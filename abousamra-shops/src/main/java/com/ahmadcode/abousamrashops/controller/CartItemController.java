package com.ahmadcode.abousamrashops.controller;

import com.ahmadcode.abousamrashops.exception.ResourceNotFoundException;
import com.ahmadcode.abousamrashops.model.Cart;
import com.ahmadcode.abousamrashops.model.User;
import com.ahmadcode.abousamrashops.repository.UserRepository;
import com.ahmadcode.abousamrashops.response.ApiResponse;
import com.ahmadcode.abousamrashops.service.cart.ICartItemService;
import com.ahmadcode.abousamrashops.service.cart.ICartService;
import com.ahmadcode.abousamrashops.service.user.IUserService;
import com.ahmadcode.abousamrashops.service.user.UserService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.jar.JarException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

   @Autowired
    private ICartItemService cartItemService;

   @Autowired
   private ICartService cartService;
   @Autowired
   private IUserService userService;
   @Autowired
   private UserRepository userRepository;

   @PostMapping("/item/add")
   public ResponseEntity<ApiResponse> addItemToCart(@RequestParam  Long productId, @RequestParam  Integer quantity){
       try {
           User user = userService.getAuthenticatedUser();
           Cart  cart = cartService.initializeNewCart(user);

           cartItemService.addCartItemToCart(cart.getId(), productId, quantity);
           return ResponseEntity.ok(new ApiResponse("Add Item Success", null));
       }catch (ResourceNotFoundException e){
           return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
       }catch (JwtException e){
          return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
       }
   }

    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable  Long cartId , @PathVariable  Long itemId, @RequestParam  Integer quantity){
        try {
            cartItemService.addCartItemToCart(cartId, itemId, quantity);
            return ResponseEntity.ok(new ApiResponse("Update Item Success", null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable  Long cartId , @PathVariable  Long itemId){
        try {
            cartItemService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("Remove Item Success", null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }




}
