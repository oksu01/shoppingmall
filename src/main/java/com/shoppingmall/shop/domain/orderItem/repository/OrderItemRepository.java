package com.shoppingmall.shop.domain.orderItem.repository;

import com.shoppingmall.shop.domain.orderItem.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
