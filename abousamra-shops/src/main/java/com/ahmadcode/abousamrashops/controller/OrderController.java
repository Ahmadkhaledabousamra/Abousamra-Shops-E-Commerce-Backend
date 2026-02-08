package com.ahmadcode.abousamrashops.controller;

import com.ahmadcode.abousamrashops.dto.OrderDto;
import com.ahmadcode.abousamrashops.exception.ResourceNotFoundException;
import com.ahmadcode.abousamrashops.model.Order;
import com.ahmadcode.abousamrashops.response.ApiResponse;
import com.ahmadcode.abousamrashops.service.order.IOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;


    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId){
        try{
            Order order = orderService.placeOrder(userId);
            OrderDto orderDto = orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("Item Order Success!",orderDto));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
       try{
           OrderDto order = orderService.getOrder(orderId);
           return ResponseEntity.ok(new ApiResponse("Sucsess!",order));
       }catch (ResourceNotFoundException e){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Ooops!",e.getMessage()));
       }
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId){
        try{
            List<OrderDto> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Sucsess!",orders));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Ooops!",e.getMessage()));
        }
    }

}
