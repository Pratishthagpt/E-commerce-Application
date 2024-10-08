package dev.pratishtha.project.productService.services;

import dev.pratishtha.project.productService.dtos.GenericProductDTO;
import dev.pratishtha.project.productService.dtos.SearchAndSortProductDTO;
import dev.pratishtha.project.productService.dtos.SearchProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface SearchService {
    Page<GenericProductDTO> getProductsByTitleSearch(String token, SearchProductDTO searchProductDTO);

    Page<GenericProductDTO> getProductsBySearchingSortedTitleAndInventoryCount(String token, SearchAndSortProductDTO searchAndSortProductDTO);

}
