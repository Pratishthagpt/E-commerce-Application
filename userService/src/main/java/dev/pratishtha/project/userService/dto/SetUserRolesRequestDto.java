package dev.pratishtha.project.userService.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SetUserRolesRequestDto {

    private List<String> roleNames;
}
