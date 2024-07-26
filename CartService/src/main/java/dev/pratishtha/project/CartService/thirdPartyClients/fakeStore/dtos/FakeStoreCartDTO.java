package dev.pratishtha.project.CartService.thirdPartyClients.fakeStore.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FakeStoreCartDTO {

    private String id;
    private String userId;
    private Date date;
    private List<FakeStoreCartItemDTO> products;

}
