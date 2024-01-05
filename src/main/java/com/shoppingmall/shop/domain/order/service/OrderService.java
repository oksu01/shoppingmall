package com.shoppingmall.shop.domain.order.service;

import com.shoppingmall.shop.constant.OrderStatus;
import com.shoppingmall.shop.domain.item.entity.Item;
import com.shoppingmall.shop.domain.item.repository.ItemRepository;
import com.shoppingmall.shop.domain.member.entity.Member;
import com.shoppingmall.shop.domain.member.repository.MemberRepository;
import com.shoppingmall.shop.domain.order.dto.OrderHistory;
import com.shoppingmall.shop.domain.order.dto.OrderInfo;
import com.shoppingmall.shop.domain.order.entity.Order;
import com.shoppingmall.shop.domain.order.repository.OrderRepository;
import com.shoppingmall.shop.domain.orderItem.entity.OrderItem;
import com.shoppingmall.shop.domain.orderItem.repository.OrderItemRepository;
import com.shoppingmall.shop.global.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;


    // 주문 생성
    public Long createOrder(OrderInfo orderInfo, Long loginId) {

        Item item = itemRepository.findById(orderInfo.getItemId()).orElseThrow(OutOfStockProductException::new);
        Member member = validateMember(loginId);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderInfo.getCount());
        orderItemList.add(orderItem);
        
        evaluateQuantity(orderInfo.getCount());

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getOrderId();
    }

    // 주문 목록 조회
    public Page<OrderHistory> getOrderList(Long loginId, Pageable pageable) {

        List<Order> orders = orderRepository.findOrders(loginId, pageable);
        Long totalCount = orderRepository.countOrder(loginId);

        return new PageImpl<>(orders.stream()
                .map(order -> OrderHistory.builder()
                        .orderId(order.getOrderId())
                        .orderDate(String.valueOf(order.getOrderDate()))
                        .orderStatus(OrderStatus.ORDER)
                        .build())
                .collect(Collectors.toList()), pageable, totalCount);
    }

    // 주문 취소
    public void cancelOrder(Long orderId, Long loginId) {

        Member member = validateMember(loginId);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        order.cancelOrder();
    }

    // 주문 여러건 생성
    public Long createOrders(List<OrderInfo> orderList, Long loginId) {

        Member member = validateMember(loginId);
        List<OrderItem> orderItemList = new ArrayList<>();

        for(OrderInfo orderInfo : orderList) { // 주문할 상품 리스트 목록 생성
            Item item = itemRepository.findById(orderInfo.getItemId()).orElseThrow(ItemNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderInfo.getCount()); // 주문할 상품 생성
            orderItemList.add(orderItem); // 리스트에 주문할 상품들 추가
        }
        Order order = Order.createOrder(member, orderItemList); // 주문 생성
        orderRepository.save(order);

        return order.getOrderId();
    }

    public Long orders(List<OrderInfo> orderDtoList, Long loginId) { // 주문들 생성
        Member member = memberRepository.findById(loginId).orElseThrow(OrderNotValidException::new);
        List<OrderItem> orderItemList = new ArrayList<>();

        for(OrderInfo orderDto : orderDtoList) { // 주문할 상품 리스트 목록 생성
            Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount()); // 주문할 상품 생성
            orderItemList.add(orderItem); // 리스트에 주문할 상품들 추가
        }
        Order order = Order.createOrder(member, orderItemList); // 주문 생성
        orderRepository.save(order);

        return order.getOrderId();
    }

    private Member validateMember(Long loginId) {
        return memberRepository.findById(loginId).orElseThrow(MemberNotFoundException::new);
    }

    private void evaluateQuantity(int count) {
        if(count < 1 || count > 999) {
            throw new OrderNotValidException();
        }
    }
}
