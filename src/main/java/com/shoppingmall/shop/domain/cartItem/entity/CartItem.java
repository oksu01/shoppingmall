package com.shoppingmall.shop.domain.cartItem.entity;


import com.shoppingmall.shop.domain.cart.entity.Cart;
import com.shoppingmall.shop.domain.item.entity.Item;
import com.shoppingmall.shop.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long cartItemId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item")
    private Item item;

    private int cartItemCount;


    // 장바구니 수량 증가 로직
    public void addCount(int cartItemCount) {
        this.cartItemCount += cartItemCount;
    }

    public static CartItem createCartItem(Cart cart, Item item, int cartItemCount) {

        return CartItem.builder()
                .cart(cart)
                .item(item)
                .cartItemCount(cartItemCount)
                .build();
    }

    public void updateCount(int cartItemCount) { // 장바구니상품의 수량을 업데이트
        this.cartItemCount = cartItemCount;
    }


}
