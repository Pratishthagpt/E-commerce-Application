package dev.pratishtha.project.productService.controllers.ProductController;

import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import dev.pratishtha.project.productService.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @PostMapping
    public GenericProductDTO addNewProduct (@RequestBody GenericProductDTO genericProductDTO) {
        return productService.createNewProduct(genericProductDTO);
    }
}
