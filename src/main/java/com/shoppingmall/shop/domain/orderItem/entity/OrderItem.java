package com.shoppingmall.shop.domain.orderItem.entity;

import com.shoppingmall.shop.domain.item.entity.Item;
import com.shoppingmall.shop.domain.order.entity.Order;
import com.shoppingmall.shop.global.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


@Entity
@Getter
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;


    public static OrderItem createOrderItem(Item item, int count) {

        item.removeStock(count);

        return OrderItem.builder()
                .item(item)
                .count(count)
                .orderPrice(item.getPrice())
                .build();
    }

    public void cancel() {
        this.getItem().addStock(count);
    }
}
