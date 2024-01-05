package com.shoppingmall.shop.global.exception;

import org.springframework.http.HttpStatus;

public class MemberNotUpdatedException extends MemberException{

    public static final String MESSAGE = "변경사항이 없습니다.";
    public static final String CODE = "MEMBER-400";

    public MemberNotUpdatedException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}

