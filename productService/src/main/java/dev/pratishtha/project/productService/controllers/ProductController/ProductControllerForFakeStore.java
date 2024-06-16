package dev.pratishtha.project.productService.controllers.ProductController;

import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import dev.pratishtha.project.productService.exceptions.CategoryNotFoundException;
import dev.pratishtha.project.productService.exceptions.IdNotFoundException;
import dev.pratishtha.project.productService.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fakestore/products")
public class ProductControllerForFakeStore {

    private ProductService productService;

    @Autowired
    public ProductControllerForFakeStore(@Qualifier("fakeStoreProductServiceImpl") ProductService productService) {
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
    public List<GenericProductDTO> getProductsWithLimit (@PathVariable ("limit") int limit) {
        return productService.getProductsWithLimit(limit);
    }

    @GetMapping("/sort/{sortType}")
    public List<GenericProductDTO> getProductByIdWithSort (@PathVariable ("sortType") String sortType) throws IdNotFoundException {
        return productService.getAllProductsWithSort(sortType);
    }

    @GetMapping("/categories")
    public List<String> getAllCategories () {
        return productService.getAllCategories();
    }

    @GetMapping("/category/{category-name}")
    public List<GenericProductDTO> getAllProductsByCategory (@PathVariable ("category-name") String category) throws CategoryNotFoundException {
        return productService.getAllProductsByCategory(category);
    }

    @PostMapping
    public GenericProductDTO addNewProduct (@RequestBody GenericProductDTO genericProductDTO) {
        return productService.createNewProduct(genericProductDTO);
    }

    @PutMapping("{id}")
    public GenericProductDTO updateProductById (@PathVariable ("id") String id, @RequestBody GenericProductDTO genericProductRequest) {
        return productService.updateProductById(id, genericProductRequest);
    }

    @PatchMapping("{id}")
    public GenericProductDTO updateSubProductById (@PathVariable ("id") String id, @RequestBody GenericProductDTO genericProductRequest) {
        return productService.updateSubProductById(id, genericProductRequest);
    }

    @DeleteMapping("{id}")
    public GenericProductDTO deleteProductById(@PathVariable ("id") String id) throws IdNotFoundException {
        return productService.deleteProductById(id);
    }
}
