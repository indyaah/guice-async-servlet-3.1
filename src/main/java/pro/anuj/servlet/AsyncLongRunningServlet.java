package pro.anuj.servlet;

import com.google.gson.Gson;
import com.google.inject.Injector;
import lombok.extern.log4j.Log4j2;
import pro.anuj.AppConstants;
import pro.anuj.config.WebAsyncListener;
import pro.anuj.service.AsyncRequestProcessor;
import pro.anuj.service.DummyService;

import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

@Log4j2
@WebServlet(urlPatterns = "/hola/*", asyncSupported = true)
public class AsyncLongRunningServlet extends HttpServlet {

    private DummyService dummyService;

    public AsyncLongRunningServlet() {
        super();
    }

    @Inject
    void setDummyService(DummyService dummyService) {
        this.dummyService = dummyService;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        final Injector injector = (Injector) config.getServletContext().getAttribute(Injector.class.getName());
        injector.injectMembers(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        final String time = request.getParameter("time");
        try {
            final int millis = Integer.valueOf(time);
            final AsyncContext asyncCtx = getAsyncContext(request);
            final ThreadPoolExecutor executor = (ThreadPoolExecutor) request.getServletContext()
                    .getAttribute(AppConstants.AYSNC_SERVLET_EXECUTOR_THREADPOOL);
            AsyncRequestProcessor command = new AsyncRequestProcessor(asyncCtx, millis, dummyService);

            executor.submit(command);

        } catch (NumberFormatException ne) {
            ErrorResponse errorResponse = new ErrorResponse("Invalid time specified, please provide a valid number");
            response.getWriter().write(new Gson().toJson(errorResponse));
            response.setContentType(AppConstants.CONTENT_TYPE);
            response.setStatus(AppConstants.INVALID_REQUEST_STATUS);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse("Could not fulfill request, please try again.");
            response.getWriter().write(new Gson().toJson(errorResponse));
            response.setContentType(AppConstants.CONTENT_TYPE);
            response.setStatus(AppConstants.INTERNAL_ERROR_STATUS);
        }
        long endTime = System.currentTimeMillis();
        log.debug("AsyncLongRunningServlet Ended : Name={}, ID={}, Time Taken= {} ms.",
                Thread.currentThread().getName(),
                Thread.currentThread().getId(),
                (endTime - startTime));
    }

    private AsyncContext getAsyncContext(HttpServletRequest request) {
        AsyncContext asyncCtx = request.startAsync();
        asyncCtx.addListener(new WebAsyncListener());
        asyncCtx.setTimeout(AppConstants.ASYNC_SERVLET_HARD_TIMEOUT);
        return asyncCtx;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("POST requests not supported.");
    }

}