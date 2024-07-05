package dev.pratishtha.project.userService.services;

import dev.pratishtha.project.userService.dto.UserDto;
import dev.pratishtha.project.userService.exceptions.UserNotFoundException;
import dev.pratishtha.project.userService.models.User;
import dev.pratishtha.project.userService.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
