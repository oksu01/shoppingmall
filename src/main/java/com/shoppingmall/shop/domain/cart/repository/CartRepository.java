package com.shoppingmall.shop.domain.cart.repository;

import com.shoppingmall.shop.domain.cart.entity.Cart;
import com.shoppingmall.shop.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMember(Member member);

}
