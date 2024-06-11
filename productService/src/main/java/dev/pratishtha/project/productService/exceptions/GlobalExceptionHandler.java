package dev.pratishtha.project.productService.exceptions;


import dev.pratishtha.project.productService.dtos.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleIdNotFoundException (
            IdNotFoundException idNotFoundException) {

        return new ResponseEntity<>(
                new ExceptionDTO(HttpStatus.NOT_FOUND, idNotFoundException.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}
