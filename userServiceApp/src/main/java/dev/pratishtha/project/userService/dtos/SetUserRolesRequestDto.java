package dev.pratishtha.project.userService.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SetUserRolesRequestDto {

    private List<String> roleNames;
}
