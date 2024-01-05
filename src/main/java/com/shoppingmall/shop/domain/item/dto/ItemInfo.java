package com.shoppingmall.shop.domain.item.dto;


import com.shoppingmall.shop.constant.ItemStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemInfo {

    private Long itemId;

    @NotBlank(message = "상품명은 필수 입력 값입니다")
    private String itemName;

    @NotNull(message = "가격은 필수 입력 값입니다")
    private Integer price;

    @NotBlank(message = "이름은 필수 입력 값입니다")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다")
    private Integer stockCount;

    private ItemStatus itemStatus;

}
