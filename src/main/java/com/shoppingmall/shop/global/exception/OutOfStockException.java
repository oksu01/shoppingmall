package com.shoppingmall.shop.global.exception;

import org.springframework.http.HttpStatus;

public abstract class OutOfStockException extends BusinessException {

    protected OutOfStockException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
