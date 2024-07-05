package dev.pratishtha.project.userService.repositories;

import dev.pratishtha.project.userService.models.Session;
import dev.pratishtha.project.userService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

}
