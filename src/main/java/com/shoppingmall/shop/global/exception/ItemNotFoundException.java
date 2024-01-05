package com.shoppingmall.shop.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ItemNotFoundException extends OrderException {

    public static final String MESSAGE = "상품이 담겨있지 않습니다.";
    public static final String CODE = "ITEM-401";

    public ItemNotFoundException() {
        super(CODE, HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
