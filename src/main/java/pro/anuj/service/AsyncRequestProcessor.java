package pro.anuj.service;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import pro.anuj.AppConstants;
import pro.anuj.servlet.AppResponse;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class AsyncRequestProcessor implements Runnable {

    private final DummyService dummyService;
    private final Gson gson = new Gson();
    private final int millis;
    private AsyncContext asyncContext;

    public AsyncRequestProcessor(AsyncContext asyncCtx, int millis, DummyService dummyService) {
        this.asyncContext = asyncCtx;
        this.millis = millis;
        this.dummyService = dummyService;
    }

    @Override
    public void run() {
        try {
            // wait for given time before finishing
            Thread.sleep(millis);

            AppResponse src = new AppResponse(millis, dummyService.getMessage(),
                    asyncContext.getRequest().isAsyncSupported());
            ServletResponse response = asyncContext.getResponse();
            response.setContentType(AppConstants.CONTENT_TYPE);
            PrintWriter out = response.getWriter();
            out.write(gson.toJson(src));
        } catch (IllegalStateException | InterruptedException | IOException e) {
            log.error("Exception while processing async request, {}", e.getLocalizedMessage());
        } finally {
            asyncContext.complete();
        }

    }

}