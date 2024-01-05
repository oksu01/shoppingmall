package com.shoppingmall.shop.domain.member.service;


import com.shoppingmall.shop.domain.cart.entity.Cart;
import com.shoppingmall.shop.domain.cart.repository.CartRepository;
import com.shoppingmall.shop.domain.member.dto.MemberInfo;
import com.shoppingmall.shop.domain.member.dto.MemberPassword;
import com.shoppingmall.shop.domain.member.entity.Member;
import com.shoppingmall.shop.domain.member.repository.MemberRepository;
import com.shoppingmall.shop.global.exception.OrderNotValidException;
import com.shoppingmall.shop.global.exception.MemberDuplicateException;
import com.shoppingmall.shop.global.exception.MemberPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;



    public void updatePassword(MemberPassword updatePassword, Long loginId) {
        Member member = validateMember(loginId);

        String password = member.getPassword();
        String newPassword = passwordEncoder.encode(updatePassword.getNewPassword());

        validatePassword(updatePassword.getPrevPassword(), password);

        member.updatePassword(newPassword);
    }

    public void updateNickname(MemberInfo updateNickname, Long loginId) {

        Member member = validateMember(loginId);

        member.updateNickname(updateNickname.getNickName());
    }

    public void validatePassword(String password, String encodedPassword) {
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new MemberPasswordException();
        }
    }

    public void deleteMember(Long loginId) {
        Member member = validateMember(loginId);
        Cart cart = validateCart(member.getCart());

        cartRepository.delete(cart);
        memberRepository.delete(member);

    }

    private Cart validateCart(Cart cart) {
        return cartRepository.findById(cart.getCartId()).orElseThrow(OrderNotValidException::new);
    }

    private Member validateMember(Long loginId) {
         return memberRepository.findById(loginId).orElseThrow(OrderNotValidException::new);
    }

}
