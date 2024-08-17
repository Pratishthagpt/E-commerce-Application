package dev.pratishtha.project.productService.controllers.ProductController;

import dev.pratishtha.project.productService.dtos.ExceptionDTO;
import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import dev.pratishtha.project.productService.exceptions.CategoryNotFoundException;
import dev.pratishtha.project.productService.exceptions.IdNotFoundException;
import dev.pratishtha.project.productService.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Product management from Fakestore API.")
@RestController
@RequestMapping("/fakestore/products")
public class ProductControllerForFakeStore {

    private ProductService productService;

    @Autowired
    public ProductControllerForFakeStore(@Qualifier("fakeStoreProductServiceImpl") ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "API for getting all products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all products.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericProductDTO[].class))
                    })
    })
    @GetMapping()
    public List<GenericProductDTO> getAllProducts () {
        return productService.getAllProducts();
    }

    @Operation(summary = "API for getting product by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get product by ID.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericProductDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Product not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @GetMapping("{id}")
    public GenericProductDTO getProductById (
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken,
            @PathVariable ("id") String id) throws IdNotFoundException {
        return productService.getProductsById(authToken, id);
    }

    @Operation(summary = "API for getting all products by limit.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all products within limit.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericProductDTO[].class))
                    })
    })
    @GetMapping("/limit/{limit}")
    public List<GenericProductDTO> getProductsWithLimit (@PathVariable ("limit") int limit) {
        return productService.getProductsWithLimit(limit);
    }

    @Operation(summary = "API for getting all products sorted by product id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all products sorted by product id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericProductDTO[].class))
                    })
    })
    @GetMapping("/sort/{sortType}")
    public List<GenericProductDTO> getAllProductsWithSort (@PathVariable ("sortType") String sortType) throws IdNotFoundException {
        return productService.getAllProductsWithSortById(sortType);
    }

    @Operation(summary = "API for getting all categories for products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all categories.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String[].class))
                    })
    })
    @GetMapping("/categories")
    public List<String> getAllCategories () {
        return productService.getAllCategories();
    }

    @Operation(summary = "API for getting all products by category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all products by category.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericProductDTO[].class))
                    }),
            @ApiResponse(responseCode = "404", description = "Category not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @GetMapping("/category/{category-name}")
    public List<GenericProductDTO> getAllProductsByCategory (@PathVariable ("category-name") String category) throws CategoryNotFoundException {
        return productService.getAllProductsByCategory(category);
    }

    @Operation(summary = "API for adding new product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Add new product.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericProductDTO.class))
                    })
    })
    @PostMapping
    public ResponseEntity<GenericProductDTO> addNewProduct (@RequestBody GenericProductDTO genericProductDTO) {
        GenericProductDTO createdProduct = productService.createNewProduct(genericProductDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @Operation(summary = "API for updating product by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update product by id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericProductDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Product not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PutMapping("{id}")
    public ResponseEntity<GenericProductDTO> updateProductById (@PathVariable ("id") String id, @RequestBody GenericProductDTO genericProductRequest) throws IdNotFoundException {
        GenericProductDTO productDTO = productService.updateProductById(id, genericProductRequest);

        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @Operation(summary = "API for updating some part of product by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update some part of product by id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericProductDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Product not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PatchMapping("{id}")
    public ResponseEntity<GenericProductDTO> updateSubProductById (@PathVariable ("id") String id, @RequestBody GenericProductDTO genericProductRequest) throws IdNotFoundException {
        GenericProductDTO productDTO = productService.updateSubProductById(id, genericProductRequest);

        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @Operation(summary = "API for deleting product by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete product by id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericProductDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Product not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @DeleteMapping("{id}")
    public GenericProductDTO deleteProductById(@PathVariable ("id") String id) throws IdNotFoundException {
        return productService.deleteProductById(id);
    }
}
