package dev.pratishtha.project.productService.thirdPartyClents.fakeStore.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FakeStoreProductDTO {

    private Long id;
    private String title;
    private double price;
    private String description;
    private String category;
    private String image;
}
