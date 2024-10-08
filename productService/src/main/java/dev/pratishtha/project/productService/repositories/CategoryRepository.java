package dev.pratishtha.project.productService.repositories;

import dev.pratishtha.project.productService.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    public Optional<Category> findByName (String name);
}
