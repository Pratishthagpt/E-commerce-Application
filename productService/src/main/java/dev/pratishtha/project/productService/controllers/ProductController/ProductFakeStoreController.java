package dev.pratishtha.project.productService.controllers.ProductController;

import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import dev.pratishtha.project.productService.exceptions.IdNotFoundException;
import dev.pratishtha.project.productService.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fakestore/products")
public class ProductFakeStoreController {

    private ProductService productService;

    @Autowired
    public ProductFakeStoreController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public List<GenericProductDTO> getAllProducts () {
        return productService.getAllProducts();
    }

    @GetMapping("{id}")
    public GenericProductDTO getProductById (@PathVariable ("id") String id) throws IdNotFoundException {
        return productService.getProductsById(id);
    }

    @GetMapping("/limit/{limit}")
    public List<GenericProductDTO> getProductByIdWithLimit (@PathVariable ("limit") int limit) throws IdNotFoundException {
        return productService.getProductByIdWithLimit(limit);
    }

    @GetMapping("/sort/{sortType}")
    public List<GenericProductDTO> getProductByIdWithSort (@PathVariable ("sortType") String sortType) throws IdNotFoundException {
        return productService.getProductByIdWithSort(sortType);
    }

    @GetMapping("/categories")
    public List<String> getAllCategories () {
        return productService.getAllCategories();
    }
}
