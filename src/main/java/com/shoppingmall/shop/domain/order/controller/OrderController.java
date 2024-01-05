package com.shoppingmall.shop.domain.order.controller;


import com.shoppingmall.shop.domain.order.dto.OrderHistory;
import com.shoppingmall.shop.domain.order.dto.OrderInfo;
import com.shoppingmall.shop.domain.order.entity.Order;
import com.shoppingmall.shop.domain.order.service.OrderService;
import com.shoppingmall.shop.global.annotation.LoginId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RequestMapping("/orders")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/")
    public ResponseEntity<Long> createdOrder(@RequestBody OrderInfo orderInfo,
                                             @LoginId Long loginId) {

        Long orderId = orderService.createOrder(orderInfo, loginId);

        return ResponseEntity.ok(orderId);
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<Page<OrderHistory>> getOrderList(@LoginId Long loginId,
                                                           @RequestParam Pageable pageable) {

        Page<OrderHistory> orderHistories = orderService.getOrderList(loginId, pageable);

        return ResponseEntity.ok(orderHistories);
    }

    @PostMapping("/{order-id}")
    public ResponseEntity<Void> cancelOrder(@Positive @PathVariable Long orderId,
                                            @LoginId Long loginId) {

        orderService.cancelOrder(orderId, loginId);

        return ResponseEntity.noContent().build();
    }

}
