package com.shoppingmall.shop.domain.order.entity;


import com.shoppingmall.shop.constant.OrderStatus;
import com.shoppingmall.shop.domain.item.entity.Item;
import com.shoppingmall.shop.domain.member.entity.Member;
import com.shoppingmall.shop.domain.orderItem.entity.OrderItem;
import com.shoppingmall.shop.global.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.*;
import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long orderId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(value = STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList;



    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        return Order.builder()
                .member(member)
                .orderItemList(orderItemList)
                .orderStatus(OrderStatus.ORDER)
                .orderDate(LocalDateTime.now())
                .build();
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;
        for(OrderItem orderItem : orderItemList) {
            orderItem.cancel();
        }
    }

}
