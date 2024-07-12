package dev.pratishtha.project.userService.exceptions;

import dev.pratishtha.project.userService.dtos.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, SessionNotFoundException.class})
    public ResponseEntity<ExceptionDto> handleNotFoundException(Exception exception) {
        String message;
        HttpStatus status;

        if (exception instanceof UserNotFoundException) {
            UserNotFoundException userNotFoundException = (UserNotFoundException) exception;
            message = userNotFoundException.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else if (exception instanceof SessionNotFoundException) {
            SessionNotFoundException sessionNotFoundException = (SessionNotFoundException) exception;
            message = sessionNotFoundException.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else {
            message = "An unexpected error occurred.";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(new ExceptionDto(message, status), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleAlreadyPresentException.class)
    public ResponseEntity<ExceptionDto> handlePresentException (Exception e) {
        String message;
        HttpStatus status;

        if (e instanceof RoleAlreadyPresentException) {
            RoleAlreadyPresentException roleAlreadyPresentException = (RoleAlreadyPresentException) e;
            message = roleAlreadyPresentException.getMessage();
            status = HttpStatus.ALREADY_REPORTED;
        }
        else {
            message = "An unexpected error occurred.";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(new ExceptionDto(message, status), HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler({InvalidPasswordException.class, InvalidTokenException.class})
    public ResponseEntity<ExceptionDto> handleInvalidEntryException (Exception e) {
        String message;
        HttpStatus status;

        if (e instanceof InvalidPasswordException) {
            InvalidPasswordException invalidPasswordException = (InvalidPasswordException) e;
            message = invalidPasswordException.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if (e instanceof InvalidTokenException) {
            InvalidTokenException invalidTokenException = (InvalidTokenException) e;
            message = invalidTokenException.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else {
            message = "An unexpected error occurred.";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(new ExceptionDto(message, status), HttpStatus.BAD_REQUEST);
    }


}
