package dev.pratishtha.project.CartService.exceptions;

import dev.pratishtha.project.CartService.dtos.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CartIdNotFoundException.class, CartNotPresentException.class, ProductNotFoundException.class})
    public ResponseEntity<ExceptionDTO> handleNotFoundException(Exception exception) {
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
        else if (exception instanceof ProductNotFoundException) {
            ProductNotFoundException productNotFoundException = (ProductNotFoundException) exception;
            message = productNotFoundException.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else {
            message = "An unexpected error occurred.";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(new ExceptionDTO(message, status), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidParameterException.class})
    public ResponseEntity<ExceptionDTO> handleInvalidInputException(Exception exception) {
        String message;
        HttpStatus status;

        if (exception instanceof InvalidParameterException) {
            InvalidParameterException invalidParameterException = (InvalidParameterException) exception;
            message = invalidParameterException.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else {
            message = "An unexpected error occurred.";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(new ExceptionDTO(message, status), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidUserAuthenticationException.class, UnauthorizedUserAccessException.class})
    public ResponseEntity<ExceptionDTO> handleInvalidUserAuthenticationException (Exception exception) {
        String message;
        HttpStatus status;

        if (exception instanceof InvalidUserAuthenticationException) {
            InvalidUserAuthenticationException invalidUserAuthenticationException = (InvalidUserAuthenticationException) exception;
            message = invalidUserAuthenticationException.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        }
        else if (exception instanceof UnauthorizedUserAccessException) {
            UnauthorizedUserAccessException unauthorizedUserAccessException = (UnauthorizedUserAccessException) exception;
            message = unauthorizedUserAccessException.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        }
        else {
            message = "An unexpected error occurred.";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(new ExceptionDTO(message, status), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({InsufficientProductQuantityException.class})
    public ResponseEntity<ExceptionDTO> handleInsufficientQuantityException (Exception exception) {
        String message;
        HttpStatus status;

        if (exception instanceof InsufficientProductQuantityException) {
            InsufficientProductQuantityException insufficientProductQuantityException = (InsufficientProductQuantityException) exception;
            message = insufficientProductQuantityException.getMessage();
            status = HttpStatus.CONFLICT;
        }
        else {
            message = "An unexpected error occurred.";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(new ExceptionDTO(message, status), HttpStatus.CONFLICT);
    }
}
