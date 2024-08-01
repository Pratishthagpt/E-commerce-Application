package dev.pratishtha.project.orderService.exceptions;

import dev.pratishtha.project.orderService.dtos.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InvalidUserAuthenticationException.class, UnAuthorizedUserAccessException.class})
    public ResponseEntity<ExceptionDTO> handleInvalidUserAuthenticationException (Exception exception) {
        String message;
        HttpStatus status;

        if (exception instanceof InvalidUserAuthenticationException) {
            InvalidUserAuthenticationException invalidUserAuthenticationException = (InvalidUserAuthenticationException) exception;
            message = invalidUserAuthenticationException.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        }
        else if (exception instanceof UnAuthorizedUserAccessException) {
            UnAuthorizedUserAccessException unAuthorizedUserAccessException = (UnAuthorizedUserAccessException) exception;
            message = unAuthorizedUserAccessException.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        }
        else {
            message = "An unexpected error occurred.";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(new ExceptionDTO(message, status), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AddressIdNotFoundException.class, AddressNotFoundException.class})
    public ResponseEntity<ExceptionDTO> handleNotFoundException (Exception exception) {
        String message;
        HttpStatus status;

        if (exception instanceof AddressIdNotFoundException) {
            AddressIdNotFoundException addressIdNotFoundException = (AddressIdNotFoundException) exception;
            message = addressIdNotFoundException.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else if (exception instanceof AddressNotFoundException) {
            AddressNotFoundException addressNotFoundException = (AddressNotFoundException) exception;
            message = addressNotFoundException.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else {
            message = "An unexpected error occurred.";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(new ExceptionDTO(message, status), HttpStatus.NOT_FOUND);
    }
}
