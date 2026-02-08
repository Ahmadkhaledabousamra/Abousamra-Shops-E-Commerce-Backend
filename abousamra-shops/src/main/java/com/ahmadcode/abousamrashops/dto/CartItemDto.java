package com.ahmadcode.abousamrashops.dto;

import com.ahmadcode.abousamrashops.model.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto   {
    private Long itemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDto product;

}
