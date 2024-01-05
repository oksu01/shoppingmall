package com.shoppingmall.shop.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OrderNotValidException extends OrderException {

    public static final String MESSAGE = "올바른 형식이 아닙니다.";
    public static final String CODE = "ORDER-400";

    public OrderNotValidException() {
        super(CODE, HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
