package com.ahmadcode.abousamrashops.dto;

import com.ahmadcode.abousamrashops.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private Long userId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private String status;
    private Set<OrderItemDto> items;
}
