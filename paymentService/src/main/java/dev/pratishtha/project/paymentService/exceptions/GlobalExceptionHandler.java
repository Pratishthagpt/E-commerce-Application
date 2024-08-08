package dev.pratishtha.project.paymentService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InvalidUserAuthenticationException.class})
    public ResponseEntity<ExceptionDTO> handleInvalidUserAuthenticationException (Exception exception) {
        String message;
        HttpStatus status;

        if (exception instanceof InvalidUserAuthenticationException) {
            InvalidUserAuthenticationException invalidUserAuthenticationException = (InvalidUserAuthenticationException) exception;
            message = invalidUserAuthenticationException.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        }
        else {
            message = "An unexpected error occurred.";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(new ExceptionDTO(message, status), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({OrderNotFoundException.class})
    public ResponseEntity<ExceptionDTO> handleNotFoundException (Exception exception) {
        String message;
        HttpStatus status;

        if (exception instanceof OrderNotFoundException) {
            OrderNotFoundException orderNotFoundException = (OrderNotFoundException) exception;
            message = orderNotFoundException.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else {
            message = "An unexpected error occurred.";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(new ExceptionDTO(message, status), HttpStatus.NOT_FOUND);
    }


}
