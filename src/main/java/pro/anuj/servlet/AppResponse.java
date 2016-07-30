package pro.anuj.servlet;

import lombok.Getter;

@Getter
public class AppResponse {

    private final int wait;
    private final String message;
    private final boolean asyncSupport;

    public AppResponse(int wait, String message, boolean asyncSupport) {
        this.wait = wait;
        this.message = message;
        this.asyncSupport = asyncSupport;
    }
}
