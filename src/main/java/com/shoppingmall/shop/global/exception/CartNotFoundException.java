package com.shoppingmall.shop.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CartNotFoundException extends CartException {

    public static final String MESSAGE = "장바구니가 존재하지 않습니다.";
    public static final String CODE = "CART-401";

    public CartNotFoundException() {
        super(CODE, HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
