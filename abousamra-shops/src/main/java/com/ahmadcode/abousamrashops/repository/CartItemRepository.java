package com.ahmadcode.abousamrashops.repository;

import com.ahmadcode.abousamrashops.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    void deleteAllByCartId(Long id);

    CartItem findByCartIdAndProductId(Long cartId, Long productId);
}
