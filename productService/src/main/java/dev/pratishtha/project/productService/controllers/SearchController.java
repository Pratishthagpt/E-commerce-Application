package dev.pratishtha.project.productService.controllers;

import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import dev.pratishtha.project.productService.dtos.SearchAndSortProductDTO;
import dev.pratishtha.project.productService.dtos.SearchProductDTO;
import dev.pratishtha.project.productService.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/db/products/search")
public class SearchController {

    private SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/title")
    public ResponseEntity<Page<GenericProductDTO>> searchAllProductsByTitle (
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken,
            @RequestBody SearchProductDTO searchProductDTO) {

        Page<GenericProductDTO> genericProductDTOS = searchService.getProductsByTitleSearch(authToken, searchProductDTO);

        return new ResponseEntity<>(genericProductDTOS, HttpStatus.OK);
    }

    @PostMapping("/sort/title")
    public ResponseEntity<Page<GenericProductDTO>> searchAllSortedProductsByTitleContainingAndInventoryCount (
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken,
            @RequestBody SearchAndSortProductDTO searchAndSortProductDTO
            ) {

        Page<GenericProductDTO> genericProductDTOS = searchService.getProductsBySearchingSortedTitleAndInventoryCount(authToken, searchAndSortProductDTO);

        return new ResponseEntity<>(genericProductDTOS, HttpStatus.OK);
    }
}
