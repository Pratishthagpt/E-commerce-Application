package dev.pratishtha.project.CartService.thirdPartyClients.fakeStore.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FakeStoreCartDTO {

    private String cartId;
    private String userId;
    private Date createdAt;
    private List<FakeStoreCartItemDTO> cartItems;

}
