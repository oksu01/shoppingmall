package com.shoppingmall.shop.global;


import com.shoppingmall.shop.auth.Authority;
import com.shoppingmall.shop.constant.ItemStatus;
import com.shoppingmall.shop.domain.cart.entity.Cart;
import com.shoppingmall.shop.domain.cart.repository.CartRepository;
import com.shoppingmall.shop.domain.cartItem.entity.CartItem;
import com.shoppingmall.shop.domain.cartItem.repository.CartItemRepository;
import com.shoppingmall.shop.domain.item.entity.Item;
import com.shoppingmall.shop.domain.item.repository.ItemRepository;
import com.shoppingmall.shop.domain.member.entity.Member;
import com.shoppingmall.shop.domain.member.repository.MemberRepository;
import com.shoppingmall.shop.domain.order.entity.Order;
import com.shoppingmall.shop.domain.order.repository.OrderRepository;
import com.shoppingmall.shop.domain.orderItem.entity.OrderItem;
import com.shoppingmall.shop.domain.orderItem.repository.OrderItemRepository;
import com.shoppingmall.shop.security.JwtProvider;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ServiceTest {

    @Autowired protected CartRepository cartRepository;
    @Autowired protected CartItemRepository cartItemRepository;
    @Autowired protected ItemRepository itemRepository;
    @Autowired protected MemberRepository memberRepository;
    @Autowired protected OrderItemRepository orderItemRepository;
    @Autowired protected OrderRepository orderRepository;
    @Autowired protected EntityManager em;
    @Mock protected JwtProvider jwtProvider;


    protected Member createAndSaveMember() {

        Authority userRole = Authority.builder().name("ROLE_USER").build();

        Member member = Member.builder()
                .email("test@email.com")
                .password("!1q2w3e4r")
                .nickname("hong")
                .roles(List.of(userRole))
                .build();

        memberRepository.save(member);

        return member;
    }

    protected Order createAndSaveOrder(Member member, List<OrderItem> item) {
        Order order = Order.createOrder(member, item);
        return orderRepository.save(order);
    }

    protected Cart createAndSaveCart(Member member) {
        Cart cart = Cart.createCart(member);
        return cartRepository.save(cart);
    }

    protected Item createAndSaveItem(Member member) {
        Item item = Item.builder()
                .itemName("당근")
                .price(500)
                .stockCount(10)
                .itemDetail("홍당무")
                .itemStatus(ItemStatus.SELL)
                .build();
        return itemRepository.save(item);
    }

    protected CartItem createAndSaveCartItem(Cart cart, Item item) {
        CartItem cartItem = CartItem.createCartItem(cart, item, 10);
        return cartItemRepository.save(cartItem);

    }

}
