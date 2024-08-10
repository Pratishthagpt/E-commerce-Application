package dev.pratishtha.project.productService.dtos;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchAndSortProductDTO {

    private String query;
    private int pageNumber;
    private int pageSize;
    private List<SortValue> sortTypes;
}
