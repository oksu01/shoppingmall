package com.shoppingmall.shop.domain.item.dto;


import com.shoppingmall.shop.constant.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ItemUpdate {

    private String itemName;

    private Integer price;



}
