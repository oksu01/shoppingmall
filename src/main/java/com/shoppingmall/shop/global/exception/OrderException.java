package com.shoppingmall.shop.global.exception;

import org.springframework.http.HttpStatus;

public abstract class OrderException extends BusinessException {

    protected OrderException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
