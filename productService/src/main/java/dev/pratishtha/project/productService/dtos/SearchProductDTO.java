package dev.pratishtha.project.productService.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchProductDTO {

    private String query;
    private int pageNumber;
    private int pageSize;
}
