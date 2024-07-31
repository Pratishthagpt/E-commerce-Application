package dev.pratishtha.project.orderService.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {

    private String addressId;
    private String userId;
    private String houseNo;
    private String city;
    private String state;
    private String country;
    private String pincode;
}
