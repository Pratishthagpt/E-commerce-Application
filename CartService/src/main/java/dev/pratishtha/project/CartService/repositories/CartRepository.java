package dev.pratishtha.project.CartService.repositories;

import dev.pratishtha.project.CartService.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    List<Cart> findAllByOrderByUuidDesc();

    @Query("SELECT c FROM Cart c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    List<Cart> findAllCartsInDateRange(@Param("startDate") Date startDate,@Param("endDate") Date endDate);
}
