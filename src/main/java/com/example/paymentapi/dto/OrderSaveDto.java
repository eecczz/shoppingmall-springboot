package com.example.paymentapi.dto;

import lombok.Data;

@Data
public class OrderSaveDto {
    private Long productId;
    private Long orderPrice;
    private Long orderCount;
    private String receiverName;
    private String phoneNumber;
    private String orderNumber;
    private String zipcode;
    private String address;
    private boolean orderRequired;
    private String paymentMethod;
}
