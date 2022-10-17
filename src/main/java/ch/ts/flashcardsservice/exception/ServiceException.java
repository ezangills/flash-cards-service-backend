package ch.ts.flashcardsservice.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ServiceException extends RuntimeException {
    private HttpStatus status;

    public ServiceException() {
        super();
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.status = httpStatus;
    }

    public ServiceException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ServiceException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.status = httpStatus;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
