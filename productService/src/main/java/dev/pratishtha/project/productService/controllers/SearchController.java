package dev.pratishtha.project.productService.controllers;

import dev.pratishtha.project.productService.dtos.ExceptionDTO;
import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import dev.pratishtha.project.productService.dtos.SearchAndSortProductDTO;
import dev.pratishtha.project.productService.dtos.SearchProductDTO;
import dev.pratishtha.project.productService.services.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Getting products by sorting and filtering implemented pagination.")
@RestController
@RequestMapping("/db/products/search")
public class SearchController {

    private SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @Operation(summary = "API for getting all products filtered by title.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all products filtered by title.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericProductDTO[].class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PostMapping("/title")
    public ResponseEntity<Page<GenericProductDTO>> searchAllProductsByTitle (
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken,
            @RequestBody SearchProductDTO searchProductDTO) {

        Page<GenericProductDTO> genericProductDTOS = searchService.getProductsByTitleSearch(authToken, searchProductDTO);

        return new ResponseEntity<>(genericProductDTOS, HttpStatus.OK);
    }


    @Operation(summary = "API for getting all products sorted by title and inventory count.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all products sorted by title and inventory count.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericProductDTO[].class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PostMapping("/sort/title")
    public ResponseEntity<Page<GenericProductDTO>> searchAllSortedProductsByTitleContainingAndInventoryCount (
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken,
            @RequestBody SearchAndSortProductDTO searchAndSortProductDTO
            ) {

        Page<GenericProductDTO> genericProductDTOS = searchService.getProductsBySearchingSortedTitleAndInventoryCount(authToken, searchAndSortProductDTO);

        return new ResponseEntity<>(genericProductDTOS, HttpStatus.OK);
    }
}
