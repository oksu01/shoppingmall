package com.shoppingmall.shop.domain.cart.service;



import com.shoppingmall.shop.domain.cart.dto.CartOrder;
import com.shoppingmall.shop.domain.cart.entity.Cart;
import com.shoppingmall.shop.domain.cart.repository.CartRepository;


import com.shoppingmall.shop.domain.cartItem.entity.CartItem;
import com.shoppingmall.shop.domain.cartItem.repository.CartItemRepository;
import com.shoppingmall.shop.domain.item.entity.Item;

import com.shoppingmall.shop.domain.item.repository.ItemRepository;
import com.shoppingmall.shop.domain.member.entity.Member;
import com.shoppingmall.shop.domain.member.repository.MemberRepository;
import com.shoppingmall.shop.domain.order.dto.OrderInfo;
import com.shoppingmall.shop.domain.order.service.OrderService;
import com.shoppingmall.shop.global.exception.ItemNotFoundException;
import com.shoppingmall.shop.global.exception.MemberNotFoundException;
import com.shoppingmall.shop.global.exception.OutOfStockProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final OrderService orderService;

    // 카트에 아이템 담는 로직
    public Long addCart(CartItem cartItem, Long loginId) {

        // 장바구니에 담을 엔티티 조회
        Item item = itemRepository.findById(cartItem.getCartItemId()).orElseThrow(OutOfStockProductException::new);

        // 로그인한 회원 조회
        Member member = memberRepository.findById(loginId).orElseThrow(MemberNotFoundException::new);

        Cart cart = cartRepository.findByMember(member);
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        // 카트에 이미 상품이 들어가있는지 조회
        CartItem saveCartItem = cartItemRepository.findByCartItemIdAndCartItemId(cart.getCartId(), item.getItemId());

        if (saveCartItem != null) {
            saveCartItem.addCount(cartItem.getCartItemCount()); // 이미 추가 된 상품이 있을 경우 기존 상품에 카운팅
            return saveCartItem.getCartItemId();
        } else {
            CartItem cartIem = CartItem.createCartItem(cart, item, cartItem.getCartItemCount());
            cartItemRepository.save(cartIem);
            return cartIem.getCartItemId();
        }
    }

    public void updateCartItem(Long cartItemId, int cartItemCount, Long loginMember) {

        Member member = validateMember(loginMember);

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(ItemNotFoundException::new);

        cartItem.updateCount(cartItemCount);
    }

    public void deleteCartItem(Long cartItemId, Long loginId) {

        validateMember(loginId);

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        cartItemRepository.delete(cartItem);
    }

    public Long orderCartItem(List<CartOrder> cartOrderList, Long loginId) {
        List<OrderInfo> cartOrders = new ArrayList<>();
        for (CartOrder cartOrderApi : cartOrderList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderApi.getCartItemId()).orElseThrow(ItemNotFoundException::new);

            OrderInfo orderInfo = OrderInfo.builder()
                    .itemId(cartItem.getItem().getItemId())
                    .count(cartItem.getCartItemCount())
                    .build();

            cartOrders.add(orderInfo);
        }

        Long orderId = orderService.orders(cartOrders, loginId);

        for (CartOrder cartOrder : cartOrderList) {
            CartItem cartItem = cartItemRepository.findById(cartOrder.getCartItemId()).orElseThrow(ItemNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }
        return orderId;
    }


    private Member validateMember(Long loginMember) {
        return memberRepository.findById(loginMember).orElseThrow(MemberNotFoundException::new);
    }
}
