package com.ahmadcode.abousamrashops.service.order;

import com.ahmadcode.abousamrashops.dto.OrderDto;
import com.ahmadcode.abousamrashops.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
