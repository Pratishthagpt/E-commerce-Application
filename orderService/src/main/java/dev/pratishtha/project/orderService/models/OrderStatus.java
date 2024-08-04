package dev.pratishtha.project.orderService.models;


public enum OrderStatus {

    PLACED,
    CREATED,
    DISPATCHED,
    CANCELLED,
    SHIPPED,
//    'PENDING' status in case payment failed
    PENDING,
    DELIVERED
}
