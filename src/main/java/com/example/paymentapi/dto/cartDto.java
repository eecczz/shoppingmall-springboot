package com.example.paymentapi.dto;

import com.example.paymentapi.entity.SavedItem;
import lombok.Data;

import java.util.List;

@Data
public class cartDto {
    private Long id;
    private String username;
    private List<SavedItem> savedItems;
}
