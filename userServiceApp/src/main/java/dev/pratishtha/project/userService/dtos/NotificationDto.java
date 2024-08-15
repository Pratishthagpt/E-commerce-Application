package dev.pratishtha.project.userService.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class NotificationDto {
    String message;
    HttpStatus status;
}
