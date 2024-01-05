package com.shoppingmall.shop.domain.item.service;

import com.shoppingmall.shop.constant.ItemStatus;
import com.shoppingmall.shop.domain.cart.entity.Cart;
import com.shoppingmall.shop.domain.item.dto.ItemInfo;
import com.shoppingmall.shop.domain.item.entity.Item;
import com.shoppingmall.shop.domain.item.repository.ItemRepository;
import com.shoppingmall.shop.domain.member.entity.Member;
import com.shoppingmall.shop.domain.member.repository.MemberRepository;
import com.shoppingmall.shop.global.ServiceTest;
import com.shoppingmall.shop.global.exception.ItemNotFoundException;
import com.shoppingmall.shop.global.exception.MemberNotFoundException;
import com.shoppingmall.shop.global.exception.OutOfStockProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ItemServiceTest extends ServiceTest {

    @Autowired protected ItemService itemService;

    @Test
    @DisplayName("상품을 등록 할 수 있다")
    void saveItem() {
        // given
        Member member = createAndSaveMember();
        Cart cart = createAndSaveCart(member);
        ItemInfo itemInfo = ItemInfo.builder()
                .itemId(1L)
                .itemName("당근")
                .price(500)
                .itemDetail("홍당무")
                .stockCount(3)
                .itemStatus(ItemStatus.SELL)
                .build();

        // when
        Long itemId = itemService.saveItem(itemInfo, member.getMemberId());

        // then
        assertThat(itemId).isNotNull();
    }

    @Test
    @DisplayName("상품을 조회 할 수 있다")
    void getItem() {
        // given
        Member member = createAndSaveMember();
        Item item = createAndSaveItem(member);
        Cart cart = createAndSaveCart(member);

        // when
        ItemInfo itemInfo = itemService.getItem(item.getItemId());

        // then
        assertThat(itemInfo.getItemName()).isEqualTo("당근");
        assertThat(itemInfo.getItemId()).isEqualTo(1L);
        assertThat(itemInfo.getItemDetail()).isEqualTo("홍당무");
    }

    @Test
    @DisplayName("상품을 수정 할 수 있다")
    void updateItem() {
        // given
        Member member = createAndSaveMember();
        Item item = createAndSaveItem(member);
        Cart cart = createAndSaveCart(member);
        ItemInfo itemInfo = ItemInfo.builder()
                .itemName("호박")
                .price(1000)
                .build();

        // when
        itemService.updateItem(item.getItemId(), itemInfo, member.getMemberId());

        // then
        assertThat(item.getItemName()).isEqualTo("호박");
        assertThat(item.getPrice()).isEqualTo(1000);
    }

    @Test
    @DisplayName("상품을 삭제 할 수 있다")
    void deleteItem() {
        // given
        Member member = createAndSaveMember();
        Item item = createAndSaveItem(member);

        // when
        itemService.deleteItem(item.getItemId(), member.getMemberId());

        // then
       assertThat(itemRepository.findById(item.getItemId())).isEmpty();
    }

    @Test
    @DisplayName("상품이 존재하지 않으면 예외가 발생한다")
    void notExistItem() {
        // given
        Member member = createAndSaveMember();

        // when & then
        assertThrows(OutOfStockProductException.class, () -> {
            itemService.deleteItem(9999999L, member.getMemberId());
        });
    }

    @Test
    @DisplayName("로그인하지 않은 회원은 상품을 삭제 할 수 없다")
    void UnloggedUserNotDeleteItem() {
        // given
        Member member = createAndSaveMember();
        Item item = createAndSaveItem(member);

        // when & then
        assertThrows(MemberNotFoundException.class, () -> {
            itemService.deleteItem(item.getItemId(), 9999999L);
        });
    }
}