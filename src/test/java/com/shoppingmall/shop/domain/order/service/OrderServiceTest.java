package com.shoppingmall.shop.domain.order.service;

import com.shoppingmall.shop.constant.OrderStatus;
import com.shoppingmall.shop.domain.cart.entity.Cart;
import com.shoppingmall.shop.domain.cartItem.entity.CartItem;
import com.shoppingmall.shop.domain.item.entity.Item;
import com.shoppingmall.shop.domain.member.entity.Member;
import com.shoppingmall.shop.domain.order.dto.OrderHistory;
import com.shoppingmall.shop.domain.order.dto.OrderInfo;
import com.shoppingmall.shop.domain.order.entity.Order;
import com.shoppingmall.shop.domain.orderItem.entity.OrderItem;
import com.shoppingmall.shop.global.ServiceTest;
import com.shoppingmall.shop.global.exception.MemberNotFoundException;
import com.shoppingmall.shop.global.exception.OrderNotFoundException;
import com.shoppingmall.shop.global.exception.OrderNotValidException;
import com.shoppingmall.shop.global.exception.OutOfStockProductException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest extends ServiceTest {

    @Autowired
    protected OrderService orderService;

    @Test
    @DisplayName("주문을 생성 할 수 있다")
    void createOrder() {
        // given
        Member member = createAndSaveMember();
        Cart cart = createAndSaveCart(member);
        Item item = createAndSaveItem(member);
        CartItem cartItem = createAndSaveCartItem(cart, item);

        OrderInfo orderInfo = OrderInfo.builder()
                .itemId(item.getItemId())
                .count(10)
                .build();

        // when
        Long orderId = orderService.createOrder(orderInfo, member.getMemberId());

        // then
        assertThat(orderId).isNotNull();
    }

    @Test
    @DisplayName("주문 목록을 조회할 수 있다")
    void getOrderList() {
        // given
        Member member = createAndSaveMember();
        Cart cart = createAndSaveCart(member);
        Item item = createAndSaveItem(member);
        CartItem cartItem = createAndSaveCartItem(cart, item);
        Order order = createAndSaveOrder(member, new ArrayList<>());

        // when
        Page<OrderHistory> orderHistoryPage = orderService.getOrderList(member.getMemberId(), PageRequest.of(0, 10));

        // then
        assertThat(orderHistoryPage).isNotNull();
        assertThat(orderHistoryPage.getContent()).hasSize(1);

    }

    @Test
    @DisplayName("주문을 취소 할 수 있다")
    void cancel() {
        // given
        Member member = createAndSaveMember();
        Cart cart = createAndSaveCart(member);
        Item item = createAndSaveItem(member);
        CartItem cartItem = createAndSaveCartItem(cart, item);
        Order order = createAndSaveOrder(member, new ArrayList<>());

        // when
        orderService.cancelOrder(order.getOrderId(), member.getMemberId());

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @Test
    @DisplayName("주문을 여러건 생성 할 수 있다")
    void createOrders() {
        // given
        Member member = createAndSaveMember();
        Cart cart = createAndSaveCart(member);
        Item item = createAndSaveItem(member);

        List<OrderInfo> orderInfoList = List.of(
                OrderInfo.builder()
                        .itemId(item.getItemId())
                        .count(5)
                        .build()
        );

        // when
        Long orderIds = orderService.createOrders(orderInfoList, member.getMemberId());

        // then
        assertThat(orderIds).isNotNull();
    }

    @Test
    @DisplayName("로그인한 사용자가 아니면 주문을 취소 할 수 없다")
    void UnloggedNotCancel() {
        // given
        Member member = createAndSaveMember();
        Cart cart = createAndSaveCart(member);
        Item item = createAndSaveItem(member);
        CartItem cartItem = createAndSaveCartItem(cart, item);
        Order order = createAndSaveOrder(member, new ArrayList<>());

        // when & then
        assertThrows(MemberNotFoundException.class, () -> {
            orderService.cancelOrder(order.getOrderId(), 9999999L);
        });
    }

    @Test
    @DisplayName("주문 생성시 OrderItem에 Item이 추가된다")
    void addOrderItem() {
        // given
        Member member = createAndSaveMember();
        Cart cart = createAndSaveCart(member);
        Item item = createAndSaveItem(member);
        CartItem cartItem = createAndSaveCartItem(cart, item);
        Order order = createAndSaveOrder(member, new ArrayList<>());
        OrderInfo orderInfo = OrderInfo.builder()
                .itemId(item.getItemId())
                .count(10)
                .build();

        // when
        Long createdOrderId = orderService.createOrder(orderInfo, member.getMemberId());

        // then
        Order findOrder = orderRepository.findById(createdOrderId).orElse(null);
        assertNotNull(findOrder); // 생성된 주문이 존재하는지 확인

        List<OrderItem> orderItems = findOrder.getOrderItemList();
        assertNotNull(orderItems); // 주문 내역이 존재하는지 확인

        // 주문 내역에 주문 상품이 제대로 추가되었는지 확인
        boolean containsOrderItem = orderItems.stream()
                .anyMatch(orderItem -> orderItem.getItem().getItemId().equals(item.getItemId())
                        && orderItem.getCount() == orderInfo.getCount());
        assertTrue(containsOrderItem, "주문 내역에 해당 주문 상품이 제대로 추가되지 않았습니다.");
    }

    @Test
    @DisplayName("주문 수량은 최소 1개 이상이어야 한다")
    void orderQuantityShouldBeMinimumOne() {
        // given
        Member member = createAndSaveMember();
        Cart cart = createAndSaveCart(member);
        Item item = createAndSaveItem(member);
        OrderInfo orderInfo = OrderInfo.builder()
                .itemId(item.getItemId())
                .count(0) // 0개를 주문하도록 설정
                .build();

        // when & then
        assertThrows(OrderNotValidException.class, () -> {
            orderService.createOrder(orderInfo, member.getMemberId());
        });
    }


    @Test
    @DisplayName("재고가 부족하면 예외가 발생한다")
    void orderQuantityinsufficient() {
    // given
    Member member = createAndSaveMember();
    Cart cart = createAndSaveCart(member);
    Item item = createAndSaveItem(member);
    OrderInfo orderInfo = OrderInfo.builder()
            .itemId(item.getItemId())
            .count(50)
            .build();

    // when & then
    assertThrows(OutOfStockProductException.class, () -> {
        orderService.createOrder(orderInfo, member.getMemberId());
    });
}

    @Test
    @DisplayName("주문한 아이템은 OrderStatus 가 Order 로 변경된다")
    void OrderStatusChangeOrder() {
        // given
        Member member = createAndSaveMember();
        Cart cart = createAndSaveCart(member);
        Item item = createAndSaveItem(member);
        Order order = createAndSaveOrder(member, List.of());

        // when
        Page<OrderHistory> orderHistoryPage = orderService.getOrderList(member.getMemberId(), PageRequest.of(0, 10));

        // then
        assertThat(orderHistoryPage).isNotNull();
        assertThat(orderHistoryPage.getContent()).hasSize(1);

        OrderStatus orderStatus = orderHistoryPage.getContent().get(0).getOrderStatus();
        assertThat(orderStatus).isEqualTo(OrderStatus.ORDER);
    }

    @Test
    @DisplayName("주문 취소시 취소 할 주문이 존재하지 않으면 예외가 발생한다")
    void cancelOrderNotExist() {
        // given
        Member member = createAndSaveMember();
        Cart cart = createAndSaveCart(member);
        Item item = createAndSaveItem(member);
        Order order = createAndSaveOrder(member, List.of());

        // when & then
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.cancelOrder(999999L, member.getMemberId());
        });
    }

    @Test
    @DisplayName("회원id로 주문 목록을 조회 할 수 있다")
    void getOrderListByMemberId() {
        // given
        Member member = createAndSaveMember();
        Order order = createAndSaveOrder(member, List.of());

        // when
        Page<OrderHistory> orderHistoryPage = orderService.getOrderList(member.getMemberId(), PageRequest.of(0, 10));

        // then
        assertThat(orderHistoryPage.getContent().get(0).getOrderId()).isEqualTo(order.getOrderId());
    }

    @Test
    @DisplayName("비회원은 주문을 할 수 없다")
    void UnloggedNotOrder() {
        // given
        Member member = createAndSaveMember();
        Cart cart = createAndSaveCart(member);
        Item item = createAndSaveItem(member);
        Order order = createAndSaveOrder(member, List.of());
        OrderInfo orderInfo = OrderInfo.builder()
                .itemId(cart.getCartId())
                .count(3)
                .build();

        // when & then
        assertThrows(MemberNotFoundException.class, () -> {
            orderService.createOrder(orderInfo, 9999999L);
        });
    }

    @Test
    @DisplayName("주문 취소시 주문상태가 CANCEL로 변경된다")
    void orderStatusChangeCancel() {
        // given
        Member member = createAndSaveMember();
        Cart cart = createAndSaveCart(member);
        Item item = createAndSaveItem(member);
        Order order = createAndSaveOrder(member, List.of());

        // when
        orderService.cancelOrder(order.getOrderId(), member.getMemberId());

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
    }
}