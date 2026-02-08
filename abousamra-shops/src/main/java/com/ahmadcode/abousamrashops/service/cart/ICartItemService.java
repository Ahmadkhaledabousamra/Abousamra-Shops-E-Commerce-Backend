package com.ahmadcode.abousamrashops.service.cart;

import com.ahmadcode.abousamrashops.model.CartItem;

public interface ICartItemService {
    void addCartItemToCart(Long cartId, Long productId, int quantity);

    void removeItemFromCart(Long cartId , Long productId);
    void updateItemQuantity(Long cartId , Long productId , int quantity);

    CartItem getCartItem(Long cardId, Long productId);
}
