package pro.anuj.config;

import lombok.extern.log4j.Log4j2;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import java.io.IOException;

@Log4j2
public class WebAsyncListener implements AsyncListener {

    @Override
    public void onComplete(AsyncEvent asyncEvent) throws IOException {
        log.debug("Finished processing async task");
    }

    @Override
    public void onTimeout(AsyncEvent asyncEvent) throws IOException {
        log.error("Async task ran out of time.!");
    }

    @Override
    public void onError(AsyncEvent asyncEvent) throws IOException {
        //noinspection ThrowableResultOfMethodCallIgnored
        log.error("Async task threw an exception", asyncEvent.getThrowable().getLocalizedMessage());
    }

    @Override
    public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
        log.debug("Async task started!");
    }
}
