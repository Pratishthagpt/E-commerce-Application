package dev.pratishtha.project.productService.controllers.ProductController;

import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import dev.pratishtha.project.productService.exceptions.CategoryNotFoundException;
import dev.pratishtha.project.productService.exceptions.IdNotFoundException;
import dev.pratishtha.project.productService.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/db/products")
public class ProductControllerForDatabase {

    private ProductService productService;

    @Autowired
    public ProductControllerForDatabase(@Qualifier("databaseProductServiceImpl") ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public List<GenericProductDTO> getAllProducts () {
        return productService.getAllProducts();
    }

    @PostMapping()
    public ResponseEntity<GenericProductDTO> addNewProduct (@RequestBody GenericProductDTO genericProductDTO) {
        GenericProductDTO createdProduct = productService.createNewProduct(genericProductDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<GenericProductDTO> getProductById (
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken,
            @PathVariable ("id") String id) throws IdNotFoundException {
        GenericProductDTO productDTO = productService.getProductsById(id);

        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @GetMapping("/limit/{limit}")
    public List<GenericProductDTO> getProductsWithLimit (@PathVariable ("limit") int limit){
        return productService.getProductsWithLimit(limit);
    }

    @GetMapping("/sort-by-id/{sortType}")
    public List<GenericProductDTO> getAllProductsWithSortById(@PathVariable ("sortType") String sortType) throws IdNotFoundException {
        return productService.getAllProductsWithSortById(sortType);
    }

    @GetMapping("/sort-by-title/{sortType}")
    public List<GenericProductDTO> getAllProductsWithSortByTitle(@PathVariable ("sortType") String sortType) throws IdNotFoundException {
        return productService.getAllProductsWithSortByTitle(sortType);
    }

    @GetMapping("/categories")
    public List<String> getAllCategories () {
        return productService.getAllCategories();
    }

    @GetMapping("/category/{category-name}")
    public List<GenericProductDTO> getAllProductsByCategory (@PathVariable ("category-name") String category) throws CategoryNotFoundException {
        return productService.getAllProductsByCategory(category);
    }

    @PutMapping("{id}")
    public GenericProductDTO updateProductById (@PathVariable ("id") String id, @RequestBody GenericProductDTO genericProductRequest) throws IdNotFoundException {
        return productService.updateProductById(id, genericProductRequest);
    }

    @PatchMapping("{id}")
    public GenericProductDTO updateSubProductById (@PathVariable ("id") String id, @RequestBody GenericProductDTO genericProductRequest) throws IdNotFoundException {
        return productService.updateSubProductById(id, genericProductRequest);
    }

    @DeleteMapping("{id}")
    public GenericProductDTO deleteProductById(@PathVariable ("id") String id) throws IdNotFoundException {
        return productService.deleteProductById(id);
    }
}
