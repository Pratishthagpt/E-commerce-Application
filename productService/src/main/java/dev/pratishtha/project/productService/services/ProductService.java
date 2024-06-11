package dev.pratishtha.project.productService.services;

import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    List<GenericProductDTO> getAllProducts();
}
