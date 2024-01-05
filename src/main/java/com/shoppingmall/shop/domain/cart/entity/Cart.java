package com.shoppingmall.shop.domain.cart.entity;


import com.shoppingmall.shop.domain.cartItem.entity.CartItem;
import com.shoppingmall.shop.domain.member.entity.Member;
import com.shoppingmall.shop.global.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long cartId;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = ALL, orphanRemoval = true)
    private List<CartItem> cartItemList;


    // 장바구니 생성 로직
    public static Cart createCart(Member member) {
        Cart cart = Cart.builder()
                .member(member)
                .build();
        return cart;
    }
}
