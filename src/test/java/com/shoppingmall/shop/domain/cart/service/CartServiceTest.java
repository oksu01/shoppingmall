package com.shoppingmall.shop.domain.cart.service;


import com.shoppingmall.shop.domain.cart.dto.CartOrder;
import com.shoppingmall.shop.domain.cart.entity.Cart;
import com.shoppingmall.shop.domain.cartItem.entity.CartItem;
import com.shoppingmall.shop.domain.item.entity.Item;
import com.shoppingmall.shop.domain.member.entity.Member;
import com.shoppingmall.shop.global.ServiceTest;
import com.shoppingmall.shop.global.exception.MemberNotFoundException;
import com.shoppingmall.shop.global.exception.OutOfStockProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


class CartServiceTest extends ServiceTest {

    @Autowired protected CartService cartService;


    @Test
    @DisplayName("장바구니에 상품을 담을 수 있다")
    void addCart() {
        // given
        Member member = createAndSaveMember();
        Item item = createAndSaveItem(member);
        Cart cart = createAndSaveCart(member);
        CartItem cartItem = CartItem.builder()
                .cartItemId(1L)
                .cart(cart)
                .item(item)
                .cartItemCount(3)
                .build();

        // when
        Long cartId = cartService.addCart(cartItem, member.getMemberId());

        // then
        assertThat(cartId).isNotNull();
    }

    @Test
    @DisplayName("장바구니 상품을 수정할 수 있다")
    void updateCartItemTest() {
        // given
        Member member = createAndSaveMember();
        Item item = createAndSaveItem(member);
        Cart cart = createAndSaveCart(member);

        CartItem cartItem = CartItem.builder()
                .cartItemId(1L)
                .cart(cart)
                .item(item)
                .cartItemCount(3)
                .build();
        cartItemRepository.save(cartItem);

        // when
        int updatedCount = 5;
        cartService.updateCartItem(cartItem.getCartItemId(), updatedCount, member.getMemberId());

        // then
        CartItem updatedCartItem = cartItemRepository.findById(cartItem.getCartItemId()).orElse(null);
        assertThat(updatedCartItem).isNotNull();
        assertThat(updatedCartItem.getCartItemCount()).isEqualTo(updatedCount);
    }

    @Test
    @DisplayName("장바구니 상품을 삭제 할 수 있다")
    void deleteCartItem() {
        // given
        Member member = createAndSaveMember();
        Item item = createAndSaveItem(member);
        Cart cart = createAndSaveCart(member);
        CartItem cartItem = createAndSaveCartItem(cart, item);

        // when
        cartService.deleteCartItem(cartItem.getCartItemId(), member.getMemberId());

        // then
        assertThat(cartItemRepository.findById(cartItem.getCartItemId())).isEmpty();
    }

    @Test
    @DisplayName("장바구니 상품을 주문 할 수 있다")
    void orderCartItem() {
        // given
        Member member = createAndSaveMember();
        Item item = createAndSaveItem(member);
        Cart cart = createAndSaveCart(member);
        CartItem cartItem = createAndSaveCartItem(cart, item);
        CartOrder cartOrder = CartOrder.builder()
                .cartItemId(1L)
                .cartOrderList(List.of())
                .build();

        // when
        Long orderCartItemId = cartService.orderCartItem(new ArrayList<>(), member.getMemberId());

        // then
        assertThat(orderCartItemId).isNotNull();

    }

    @Test
    @DisplayName("장바구니가 없는 회원은 상품을 담을 때 새로운 장바구니가 생성된다")
    void addCartForUserWithoutCart() {
        // given
        Member member = createAndSaveMember();
        Item item = createAndSaveItem(member);
        Cart cart = createAndSaveCart(member);
        CartItem cartItem = createAndSaveCartItem(cart, item);

        // when
        Long cartId = cartService.addCart(cartItem, member.getMemberId());

        // then
        assertThat(cartId).isNotNull();
        Cart cart1 = cartRepository.findByMember(member);
        assertThat(cart1).isNotNull();
        assertThat(cart1.getMember()).isEqualTo(member);
    }

    @Test
    @DisplayName("이미 추가된 상품이 있는 경우 기존 상품에 카운팅된다")
    void addExistingItemToCartWithCounting() {
        // given
        Member member = createAndSaveMember();
        Item item = createAndSaveItem(member);
        Cart cart = createAndSaveCart(member);

        CartItem existCartItem = CartItem.builder()
                .cartItemId(1L)
                .cart(cart)
                .item(item)
                .cartItemCount(3)
                .build();
        cartItemRepository.save(existCartItem);

        // when
        Long cartItemId = cartService.addCart(existCartItem, member.getMemberId());

        // then
        assertThat(cartItemId).isNotNull();
        CartItem savedCartItem = cartItemRepository.findByCartItemIdAndCartItemId(cart.getCartId(), item.getItemId());
        assertThat(savedCartItem).isNotNull();
        assertThat(savedCartItem.getCartItemCount()).isEqualTo(6);
    }

    @Test
    @DisplayName("장바구니 아이템 주문하기")
    void orderCartItemTest() {
        // given
        Member member = createAndSaveMember();
        Item item = createAndSaveItem(member);
        Cart cart = createAndSaveCart(member);

        CartItem cartItem = CartItem.builder()
                .cartItemId(1L)
                .cart(cart)
                .item(item)
                .cartItemCount(3)
                .build();
        cartItemRepository.save(cartItem);

        List<CartOrder> cartOrderList = new ArrayList<>();
        CartOrder cartOrder = CartOrder.builder()
                .cartItemId(cartItem.getCartItemId())
                .build();
        cartOrderList.add(cartOrder);

        // when
        Long orderId = cartService.orderCartItem(cartOrderList, member.getMemberId());

        // then
        assertThat(orderId).isNotNull();

        CartItem orderedCartItem = cartItemRepository.findById(cartItem.getCartItemId()).orElse(null);
        assertThat(orderedCartItem).isNull();
    }

    @Test
    @DisplayName("로그인하지 않은 사용자는 장바구니 상품을 수정 할 수 없다")
    void deleteCartItemWithoutLogin() {
        // given
        Member member = createAndSaveMember();
        Item item = createAndSaveItem(member);
        Cart cart = createAndSaveCart(member);

        CartItem cartItem = CartItem.builder()
                .cartItemId(1L)
                .cart(cart)
                .item(item)
                .cartItemCount(3)
                .build();
        cartItemRepository.save(cartItem);

        // when & then
        assertThrows(MemberNotFoundException.class, () -> {
            cartService.updateCartItem(cartItem.getCartItemId(), 3, 999999999L);
        });
    }

    @Test
    @DisplayName("장바구니에 담을 상품이 조회되지 않으면 'OutOfStockProductException' 이 발생한다")
    void outOfStockProductException() {
        // given
        Member member = createAndSaveMember();

        // when & then
        assertThrows(OutOfStockProductException.class, () -> {
            Long loginId = member.getMemberId();
            CartItem cartItem = CartItem.builder()
                            .cartItemId(9999999L)
                            .build();

            cartService.addCart(cartItem, loginId);
        });
    }

    @Test
    @DisplayName("장바구니에 기존 아이템이 추가되어 있지 않으면 새로운 아이템이 담긴다")
    void addNewItemToCartWhenNoExistingItem() {
        // given
        Member member = createAndSaveMember();
        Item item = createAndSaveItem(member);

        // when
        Long loginId = member.getMemberId();
        CartItem cartItem = CartItem.builder()
                .cartItemId(item.getItemId())
                .cartItemCount(100)
                .build();

        Long addItemId = cartService.addCart(cartItem, loginId);

        // then
        assertThat(addItemId).isNotNull();
    }
}
