package dev.pratishtha.project.productService.thirdPartyClents.fakeStore.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeStoreProductDTO {

    private Long id;
    private String title;
    private double price;
    private String description;
    private String category;
    private String image;
}
