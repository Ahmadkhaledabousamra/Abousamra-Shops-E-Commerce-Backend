package com.ahmadcode.abousamrashops.service.cart;

import com.ahmadcode.abousamrashops.exception.ResourceNotFoundException;
import com.ahmadcode.abousamrashops.model.Cart;
import com.ahmadcode.abousamrashops.model.User;
import com.ahmadcode.abousamrashops.repository.CartItemRepository;
import com.ahmadcode.abousamrashops.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CartService implements ICartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    private AtomicLong cartIdGenerator = new AtomicLong(0);

    @Override
    public Cart getCart(Long id) {
        Cart cart =
                cartRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Cart Not Found!"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Cart initializeNewCart(User user) {
      return Optional.ofNullable(getCartbyUserId(user.getId())).orElseGet(() -> {
                  Cart cart = new Cart();
                  cart.setUser(user);
                  return cartRepository.save(cart);
              });
    }

    @Override
    public Cart getCartbyUserId(Long userId) {
          return cartRepository.findByUserId(userId);
    }
}
