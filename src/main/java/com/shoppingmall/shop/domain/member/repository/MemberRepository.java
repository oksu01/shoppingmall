package com.shoppingmall.shop.domain.member.repository;

import com.shoppingmall.shop.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAccount(String account);

    Optional<Member> findByEmail(String email);
}