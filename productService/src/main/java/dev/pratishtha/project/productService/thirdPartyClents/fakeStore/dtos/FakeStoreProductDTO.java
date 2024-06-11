package dev.pratishtha.project.productService.thirdPartyClents.fakeStore.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeStoreProductDTO {

    private Long id;
    private String title;
    private String description;
    private String image;
    private String category_name;
    private double priceVal;
}
