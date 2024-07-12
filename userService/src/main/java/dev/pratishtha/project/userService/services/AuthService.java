package dev.pratishtha.project.userService.services;

import dev.pratishtha.project.userService.dtos.UserDto;
import dev.pratishtha.project.userService.exceptions.InvalidPasswordException;
import dev.pratishtha.project.userService.exceptions.UserNotFoundException;
import dev.pratishtha.project.userService.models.Session;
import dev.pratishtha.project.userService.models.SessionStatus;
import dev.pratishtha.project.userService.models.User;
import dev.pratishtha.project.userService.repositories.SessionRepository;
import dev.pratishtha.project.userService.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.*;

@Service
public class AuthService {

    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private SecretKey secretKey;

    @Autowired
    public AuthService(UserRepository userRepository, SessionRepository sessionRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

        secretKey = Jwts.SIG.HS256.key().build();
    }

    public UserDto signUpUser(String email, String username, String password) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        User savedUser = userRepository.save(user);
        UserDto userDto = UserDto.fromUser(savedUser);

        return userDto;
    }

    public User loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with email does not exist.");
        }
        User user = userOptional.get();

//        checking password using BCryptPasswordEncoder
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("Password does not matches.");
        }

        return user;
    }

    public Session createSession(String token, User user) {
        Session session = new Session();
        session.setUser(user);
        session.setToken(token);
        session.setSessionStatus(SessionStatus.ACTIVE);

//        keeping expiry time to 1 month after
        Date currDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currDate);
        calendar.add(Calendar.DAY_OF_MONTH, 30);

        Date expiryDate = calendar.getTime();
        session.setExpiryAt(expiryDate);

        return sessionRepository.save(session);
    }

    public void logoutUser(String userId, String token) {
//        We are designing sessions with soft delete. If the session expire or user logout, then
//        we just set the session status as ENDED and not delete the session from db. We are
//        doing this bcoz if in future we will be needing the total session login by user, then
//        we can easily compute that

//        first get the user by userId
        Optional<User> userOptional = userRepository.findById(UUID.fromString(userId));
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with email does not exist.");
        }
        User user = userOptional.get();

        Session session = sessionRepository.findByTokenAndUser(token, user);
        session.setSessionStatus(SessionStatus.ENDED);

        sessionRepository.save(session);
    }

    public SessionStatus validateUserToken(String userId, String token) {
        Optional<User> userOptional = userRepository.findById(UUID.fromString(userId));
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with email does not exist.");
        }
        User user = userOptional.get();

        Session session = sessionRepository.findByTokenAndUser(token, user);

        if (!session.getSessionStatus().equals(SessionStatus.ACTIVE)) {
            return SessionStatus.ENDED;
        }

        return SessionStatus.ACTIVE;
    }

    public String generateJwtToken(User user) {
//        we are using map for storing json claims because we want a key-value pair for it, we can go with string also
//        but it is difficult ot maintain.
        Map<String, Object> jwtData = new HashMap<>();
        LocalDate expiryDate = LocalDate.now().plusDays(15);

        Date expiryDateUtil = java.sql.Date.valueOf(expiryDate);

        jwtData.put("email", user.getEmail());
        jwtData.put("createdAt", new Date());
        jwtData.put("expiredAt", expiryDateUtil);
        jwtData.put("username", user.getUsername());

        String token = Jwts
                .builder()
                .claims(jwtData)
                .signWith(secretKey)
                .compact();

        System.out.println(secretKey.toString());

        return token;
    }
}
