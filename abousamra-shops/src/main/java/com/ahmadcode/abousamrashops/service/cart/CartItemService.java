package com.ahmadcode.abousamrashops.service.cart;

import com.ahmadcode.abousamrashops.exception.ResourceNotFoundException;
import com.ahmadcode.abousamrashops.model.Cart;
import com.ahmadcode.abousamrashops.model.CartItem;
import com.ahmadcode.abousamrashops.model.Product;
import com.ahmadcode.abousamrashops.repository.CartItemRepository;
import com.ahmadcode.abousamrashops.repository.CartRepository;
import com.ahmadcode.abousamrashops.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartItemService implements ICartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private IProductService productService;

    @Autowired
    private ICartService cartService;

    @Override
    public void addCartItemToCart(Long cartId, Long productId, int quantity) {

        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);

        CartItem optionalItem = cartItemRepository.findByCartIdAndProductId(cartId, productId);
        CartItem cartItem;

        if (optionalItem !=null) {
            cartItem = optionalItem;
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
            cart.addItem(cartItem);
        }
        cartItem.setTotalPrice();
        cart.updateTotalAmount();
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
       Cart cart = cartService.getCart(cartId);
       CartItem cartItem = getCartItem(cartId,productId);
       cart.removeItem(cartItem);
       cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
                      cart.getCartItems()
                              .stream()
                              .filter(item->item.getProduct().getId().equals(productId))
                              .findFirst()
                              .ifPresent(item -> {
                                  item.setQuantity(quantity);
                                  item.setUnitPrice(item.getProduct().getPrice());
                                  item.setTotalPrice();
                        });
        BigDecimal totalAmount = cart.getCartItems().stream()
                        .map(CartItem::getTotalPrice)
                                .reduce(BigDecimal.ZERO , BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cardId, Long productId){
        Cart cart = cartService.getCart(cardId);
        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(()->new ResourceNotFoundException("Cart Item Not Found!"));
        return cartItem;
    }

}
