package com.shoppingmall.shop.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OutOfStockProductException extends OrderException {

    public static final String MESSAGE = "재고가 부족합니다.";
    public static final String CODE = "STOCK-401";

    public OutOfStockProductException() {
        super(CODE, HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
