package com.shoppingmall.shop.global.exception;

import org.springframework.http.HttpStatus;

public abstract class CartException extends BusinessException {

    protected CartException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
