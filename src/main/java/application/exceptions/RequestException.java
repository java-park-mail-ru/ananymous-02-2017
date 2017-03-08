package application.exceptions;

import org.springframework.http.HttpStatus;

public class RequestException extends Exception {
    private HttpStatus status;

    public RequestException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
