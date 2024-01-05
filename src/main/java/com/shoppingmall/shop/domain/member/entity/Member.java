package com.shoppingmall.shop.domain.member.entity;


import com.shoppingmall.shop.auth.Authority;
import com.shoppingmall.shop.domain.cart.entity.Cart;
import com.shoppingmall.shop.domain.order.entity.Order;
import com.shoppingmall.shop.global.exception.MemberNotUpdatedException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long memberId;

    @Column(unique = true)
    private String account;

    private String password;

    private String nickname;

    private String name;

    @Column(unique = true)
    private String email;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
    private List<Order> orderList;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    public void setRoles(List<Authority> role) {
        this.roles = role;
        role.forEach(o -> o.setMember(this));
    }


    public void updatePassword(String password) {
        if (password == null || password.equals(this.password)) {
            throw new MemberNotUpdatedException();
        }

        this.password = password;
    }

    public void updateNickname(String nickname) {
        if (nickname == null || nickname.equals(this.nickname)) {
            throw new MemberNotUpdatedException();
        }

        this.nickname = nickname;
    }
}

