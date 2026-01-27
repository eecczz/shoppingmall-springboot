package com.example.paymentapi.dto;

import com.example.paymentapi.entity.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberDto {
    private Long id;
    private String nickname;
    private String username;
    private String adress;
    private List<Order> orders = new ArrayList<>();
}
