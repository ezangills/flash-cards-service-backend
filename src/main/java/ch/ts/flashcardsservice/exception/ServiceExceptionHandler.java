package ch.ts.flashcardsservice.exception;

import ch.ts.flashcardsservice.dto.MessageResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public MessageResponse handleServiceException(ServiceException e, HttpServletResponse response) {
        response.setStatus(e.getStatus().value());
        return new MessageResponse().setMessage(e.getMessage());
    }
}
