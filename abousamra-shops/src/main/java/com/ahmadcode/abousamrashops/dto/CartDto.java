package com.ahmadcode.abousamrashops.dto;

import com.ahmadcode.abousamrashops.model.CartItem;
import com.ahmadcode.abousamrashops.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
@Data
public class CartDto {
    private Long id;
    private BigDecimal totalAmount;
    private Set<CartItemDto> cartItems = new HashSet<>();
}
