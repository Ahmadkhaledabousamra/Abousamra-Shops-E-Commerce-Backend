package com.ahmadcode.abousamrashops.service.order;

import com.ahmadcode.abousamrashops.dto.OrderDto;
import com.ahmadcode.abousamrashops.enums.OrderStatus;
import com.ahmadcode.abousamrashops.exception.ResourceNotFoundException;
import com.ahmadcode.abousamrashops.model.Cart;
import com.ahmadcode.abousamrashops.model.Order;
import com.ahmadcode.abousamrashops.model.OrderItem;
import com.ahmadcode.abousamrashops.model.Product;
import com.ahmadcode.abousamrashops.repository.OrderRepository;
import com.ahmadcode.abousamrashops.repository.ProductRepository;
import com.ahmadcode.abousamrashops.service.cart.ICartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
public class OrderService implements IOrderService{


    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartbyUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order,cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order , Cart cart){
        return  cart.getCartItems().stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    product.setInventory(product.getInventory() - cartItem.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(
                            order,
                            product,
                            cartItem.getQuantity(),
                            cartItem.getUnitPrice()
                    );
                }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){
        return orderItemList.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO , BigDecimal::add);
    }



    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(()->new ResourceNotFoundException("Oder Not Found!"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId){
        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public OrderDto convertToDto(Order order){
        return modelMapper.map(order,OrderDto.class);
    }
}

