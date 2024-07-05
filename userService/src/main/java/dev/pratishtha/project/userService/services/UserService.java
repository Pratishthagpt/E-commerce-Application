package dev.pratishtha.project.userService.services;

import dev.pratishtha.project.userService.dto.RoleDto;
import dev.pratishtha.project.userService.dto.UserDto;
import dev.pratishtha.project.userService.exceptions.UserNotFoundException;
import dev.pratishtha.project.userService.models.Role;
import dev.pratishtha.project.userService.models.User;
import dev.pratishtha.project.userService.repositories.RoleRepository;
import dev.pratishtha.project.userService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserDto getUserDetailsById(String id) {
        UUID userId = UUID.fromString(id);
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with id - " + id + " not found.");
        }

        User user = userOptional.get();
        UserDto userDto = UserDto.fromUser(user);

        return userDto;
    }

    public UserDto setUserRoles(String userId, List<String> roleNames) {
        Optional<User> userOptional = userRepository.findById(UUID.fromString(userId));

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with id - " + userId + " not found.");
        }

        User user = userOptional.get();

        Set<Role> roles = new HashSet<>();
        for (String roleName: roleNames) {
            Optional<Role> role = roleRepository.findByRole(roleName);
            roles.add(role.get());
        }

        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        UserDto userDto = UserDto.fromUser(savedUser);
        userDto.setRoles(roles);

        return userDto;
    }

    public List<UserDto> findAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : userList) {
            UserDto userDto = UserDto.fromUser(user);
            userDto.setRoles(user.getRoles());
            userDtoList.add(userDto);
        }
        return userDtoList;
    }
}
