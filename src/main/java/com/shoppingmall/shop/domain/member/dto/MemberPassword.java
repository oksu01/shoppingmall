package com.shoppingmall.shop.domain.member.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberPassword {
        private String prevPassword;
        private String newPassword;
    }

