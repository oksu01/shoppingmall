package com.shoppingmall.shop.domain.cart.controller;



import com.shoppingmall.shop.domain.cart.dto.CartOrder;
import com.shoppingmall.shop.domain.cart.service.CartService;
import com.shoppingmall.shop.domain.cartItem.entity.CartItem;
import com.shoppingmall.shop.global.annotation.LoginId;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @PostMapping("/order")
    public ResponseEntity<Long> addCart(@RequestBody CartItem cartItem,
                                        @LoginId Long loginId) {

        Long cartItemId = cartService.addCart(cartItem, loginId);

        return ResponseEntity.ok(cartItemId);
    }

    @PatchMapping("/{cartItem-id}")
    public ResponseEntity<Void> updateCartItem(@Positive @PathVariable Long cartItemId,
                                               @RequestBody int cartItemCount,
                                               @LoginId Long loginId) {

        cartService.updateCartItem(cartItemId, cartItemCount, loginId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartItem-id}")
    public ResponseEntity<Void> deleteCartItem(@Positive @PathVariable Long cartItemId,
                                               @LoginId Long loginMember) {

        cartService.deleteCartItem(cartItemId, loginMember);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/orders")
    public ResponseEntity<Long> orderCartItem(@RequestBody List<CartOrder> cartOrderList,
                                              @LoginId Long loginId) {

        Long cartOrderId = cartService.orderCartItem(cartOrderList, loginId);

        return ResponseEntity.ok(cartOrderId);
    }
}
