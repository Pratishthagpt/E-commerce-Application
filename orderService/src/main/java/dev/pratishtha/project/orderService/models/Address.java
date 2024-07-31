package dev.pratishtha.project.orderService.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Address extends BaseModel{

    private String userId;
    private String houseNo;
    private String city;
    private String state;
    private String country;
    private String pincode;

}
