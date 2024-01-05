package com.shoppingmall.shop.domain.member.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberSignup {

    private String email;

    private String password;

    private String nickname;
}
