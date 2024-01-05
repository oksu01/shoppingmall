package com.shoppingmall.shop.global.exception;

import org.springframework.http.HttpStatus;

public abstract class ItemException extends BusinessException {

    protected ItemException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
