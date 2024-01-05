package com.shoppingmall.shop.domain.cartItem.repository;

import com.shoppingmall.shop.domain.cartItem.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartItemIdAndCartItemId(Long cartId, Long itemId);
}
