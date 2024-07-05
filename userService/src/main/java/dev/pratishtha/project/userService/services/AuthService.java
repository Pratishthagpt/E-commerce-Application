package dev.pratishtha.project.userService.services;

import dev.pratishtha.project.userService.dto.UserDto;
import dev.pratishtha.project.userService.models.User;
import dev.pratishtha.project.userService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto signUpUser(String email, String username, String password) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);

        User savedUser = userRepository.save(user);
        UserDto userDto = UserDto.fromUser(savedUser);

        return userDto;
    }
}
