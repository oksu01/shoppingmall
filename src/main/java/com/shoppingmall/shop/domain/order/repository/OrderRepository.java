package com.shoppingmall.shop.domain.order.repository;

import com.shoppingmall.shop.domain.order.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.member.memberId = :memberId order by o.orderDate desc")
    List<Order> findOrders(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select count(o) from Order o where o.member.memberId = :memberId")
    Long countOrder(@Param("memberId") Long memberId); // 로그인한 회원의 주문 갯수가 몇 개인지 조회
}
