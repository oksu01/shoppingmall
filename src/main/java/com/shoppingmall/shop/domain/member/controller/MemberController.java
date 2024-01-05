package com.shoppingmall.shop.domain.member.controller;


import com.shoppingmall.shop.domain.member.dto.MemberInfo;
import com.shoppingmall.shop.domain.member.dto.MemberPassword;
import com.shoppingmall.shop.domain.member.dto.MemberSignup;
import com.shoppingmall.shop.domain.member.service.MemberService;
import com.shoppingmall.shop.global.annotation.LoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/{member-id}")
    public ResponseEntity<Void> updatePassword(@RequestBody MemberPassword updatePassword,
                                               @LoginId Long loginId) {

        memberService.updatePassword(updatePassword, loginId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{member-id}/nickname")
    public ResponseEntity<Void> updateNickname(@RequestBody MemberInfo memberInfo,
                                               @LoginId Long loginId) {

        memberService.updateNickname(memberInfo, loginId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity<Void> deleteMember(@LoginId Long loginId) {

        memberService.deleteMember(loginId);

        return ResponseEntity.noContent().build();
    }


}
