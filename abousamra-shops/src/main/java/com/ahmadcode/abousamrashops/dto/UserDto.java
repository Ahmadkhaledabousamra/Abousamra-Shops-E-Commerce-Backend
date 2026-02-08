package com.ahmadcode.abousamrashops.dto;

import com.ahmadcode.abousamrashops.model.Cart;
import com.ahmadcode.abousamrashops.model.Order;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<OrderDto> orders;
    private CartDto cart;
}
