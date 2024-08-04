package dev.pratishtha.project.orderService.models;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order extends BaseModel{

    private String description;

    @OneToOne
    private Address address;

    @Enumerated(EnumType.ORDINAL)
    private OrderStatus orderStatus;
    private String paymentId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    private int totalPrice;
    private int quantity;
    private String userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

}
