package com.example.ecom.dto;

import lombok.Data;

@Data
public class AddProductInCartDto {
    private Long userId;

    private Long productId;
}
