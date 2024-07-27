package dev.pratishtha.project.CartService.exceptions;

import dev.pratishtha.project.CartService.dtos.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CartIdNotFoundException.class, CartNotPresentException.class})
    public ResponseEntity<ExceptionDTO> handleIdNotFoundException(Exception exception) {
        String message;
        HttpStatus status;

        if (exception instanceof CartIdNotFoundException) {
            CartIdNotFoundException cartIdNotFoundException = (CartIdNotFoundException) exception;
            message = cartIdNotFoundException.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else if (exception instanceof CartNotPresentException) {
            CartNotPresentException cartNotPresentException = (CartNotPresentException) exception;
            message = cartNotPresentException.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else {
            message = "An unexpected error occurred.";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(new ExceptionDTO(message, status), HttpStatus.NOT_FOUND);
    }
}
