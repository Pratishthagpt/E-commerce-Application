package dev.pratishtha.project.userService.exceptions;

import dev.pratishtha.project.userService.dto.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleNotFoundException(Exception exception) {
        String message;
        HttpStatus status;

        if (exception instanceof UserNotFoundException) {
            UserNotFoundException userNotFoundException = (UserNotFoundException) exception;
            message = userNotFoundException.getMessage();
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
}
