package com.ahmadcode.abousamrashops.service.cart;

import com.ahmadcode.abousamrashops.model.Cart;
import com.ahmadcode.abousamrashops.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);

    Cart getCartbyUserId(Long userId);
}