package dev.pratishtha.project.CartService.repositories;

import dev.pratishtha.project.CartService.models.Cart;
import dev.pratishtha.project.CartService.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
}
