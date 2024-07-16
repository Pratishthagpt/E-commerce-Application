package dev.pratishtha.project.userService.repositories;

import dev.pratishtha.project.userService.models.Session;
import dev.pratishtha.project.userService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

//    putting optional because it can be case that token is tampered and then it would be invalid
//    and hence, we won;t be able to find session according to it.
    Optional<Session> findByTokenAndUser(String token, User user);

    Optional<Session> findByToken(String token);
}
