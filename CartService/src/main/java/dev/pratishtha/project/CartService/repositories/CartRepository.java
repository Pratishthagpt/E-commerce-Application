package dev.pratishtha.project.CartService.repositories;

import dev.pratishtha.project.CartService.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    List<Cart> findAllByOrderByUuidDesc();
}
