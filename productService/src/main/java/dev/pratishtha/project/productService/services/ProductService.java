package dev.pratishtha.project.productService.services;

import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import dev.pratishtha.project.productService.exceptions.CategoryNotFoundException;
import dev.pratishtha.project.productService.exceptions.IdNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    List<GenericProductDTO> getAllProducts();

    GenericProductDTO getProductsById(String id) throws IdNotFoundException;

    List<GenericProductDTO> getProductByIdWithLimit(int limit);

    List<GenericProductDTO> getProductByIdWithSort(String sortType);

    List<String> getAllCategories();

    List<GenericProductDTO> getAllProductsByCategory(String category) throws CategoryNotFoundException;

    GenericProductDTO createNewProduct(GenericProductDTO genericProductDTO);

    GenericProductDTO updateProductById(String id, GenericProductDTO genericProductRequest);
}
