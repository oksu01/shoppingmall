package com.shoppingmall.shop.domain.item.entity;


import com.shoppingmall.shop.constant.ItemStatus;
import com.shoppingmall.shop.domain.cartItem.entity.CartItem;
import com.shoppingmall.shop.domain.item.dto.ItemInfo;
import com.shoppingmall.shop.domain.orderItem.entity.OrderItem;
import com.shoppingmall.shop.global.entity.BaseEntity;
import com.shoppingmall.shop.global.exception.OutOfStockProductException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item  {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long itemId;

    private String itemName;

    private int price;

    private int stockCount;

    private String itemDetail;

    private ItemStatus itemStatus;

    @OneToMany(mappedBy = "item", cascade = ALL, orphanRemoval = true)
    private List<CartItem> cartItemList;

    @OneToMany(mappedBy = "item", cascade = ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList;


    public static Item createItem(ItemInfo itemInfo) {

        return Item.builder()
                .itemId(itemInfo.getItemId())
                .price(itemInfo.getPrice())
                .itemDetail(itemInfo.getItemDetail())
                .stockCount(itemInfo.getStockCount())
                .itemStatus(itemInfo.getItemStatus())
                .build();
    }

    public void updateItem(String itemName, Integer price) {
        this.itemName = itemName == null ? this.itemName : itemName;
        this.price = price == null ? this.price : price;
    }

    public void removeStock(int stockCount) {
        int restStock = this.stockCount - stockCount;
        if(restStock < 0) {
            throw new OutOfStockProductException();
        }
        this.stockCount = restStock;
    }

    // 주문 수량만큼 상품 재고 증가
    public void addStock(int stockCount) {
        this.stockCount += stockCount;
    }
}
