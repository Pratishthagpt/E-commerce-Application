package dev.pratishtha.project.productService.repositories;

import dev.pratishtha.project.productService.models.Category;
import dev.pratishtha.project.productService.models.Product;
import dev.pratishtha.project.productService.models.SqlQueries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query(value = SqlQueries.GET_ALL_PRODUCTS_WITH_LIMIT, nativeQuery = true)
    List<Product> findProductsByLimit(int limit);

    List<Product> findAllByOrderByUuidDesc ();

    List<Product> findAllByOrderByTitleDesc ();

    List<Product> findAllByOrderByTitleAsc ();

    List<Product> findAllByCategory(Category category);


//    for getting pageable results
    Page<Product> findAllByTitle(String title, Pageable pageable);

    Page<Product> findAllByTitleContaining(String title, Pageable pageable);

}
