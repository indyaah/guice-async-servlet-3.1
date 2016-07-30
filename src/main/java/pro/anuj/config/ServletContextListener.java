package pro.anuj.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import lombok.extern.log4j.Log4j2;
import pro.anuj.AppConstants;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Log4j2
@WebListener
public class ServletContextListener extends GuiceServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        final ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(AppConstants.CORE_POOL_SIZE);
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(AppConstants.CORE_POOL_SIZE,
                AppConstants.MAXIMUM_POOL_SIZE,
                AppConstants.KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                workQueue);
        event.getServletContext().setAttribute(AppConstants.AYSNC_SERVLET_EXECUTOR_THREADPOOL, executor);
    }

    public void contextDestroyed(ServletContextEvent event) {
        super.contextDestroyed(event);
        final ThreadPoolExecutor executor = (ThreadPoolExecutor) event.getServletContext()
                .getAttribute(AppConstants.AYSNC_SERVLET_EXECUTOR_THREADPOOL);
        executor.shutdown();
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new AppModule());
    }
}