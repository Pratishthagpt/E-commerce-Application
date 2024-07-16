package dev.pratishtha.project.userService.security.repository;

import java.util.Optional;


import dev.pratishtha.project.userService.security.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    Optional<Client> findByClientId(String clientId);
}
