package pro.anuj.servlet;

import lombok.Getter;

@Getter class ErrorResponse {
    private final String error;

    ErrorResponse(String error) {
        this.error = error;
    }
}
