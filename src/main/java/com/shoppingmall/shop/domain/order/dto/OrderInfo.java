package com.shoppingmall.shop.domain.order.dto;


import com.shoppingmall.shop.domain.order.entity.Order;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {

    @NotNull(message = "상품 아이디는 필수 입력 값입니다")
    private Long itemId;

    @Min(value = 1, message = "최소 주문 수량은 1개 입니다")
    @Max(value = 999, message = "최대 주문 수량은 999개 입니다")
    private int count;
}
