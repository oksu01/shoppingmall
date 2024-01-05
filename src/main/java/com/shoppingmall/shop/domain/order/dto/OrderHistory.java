package com.shoppingmall.shop.domain.order.dto;

import com.shoppingmall.shop.constant.OrderStatus;
import com.shoppingmall.shop.domain.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class OrderHistory {

    public OrderHistory(Order order) {
        this.orderId = order.getOrderId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    private Long orderId;
    private String orderDate;
    private OrderStatus orderStatus;
    private List<OrderInfo> orderItemDtoList = new ArrayList<>();

}