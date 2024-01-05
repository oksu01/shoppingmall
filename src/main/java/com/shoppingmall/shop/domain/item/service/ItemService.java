package com.shoppingmall.shop.domain.item.service;


import com.shoppingmall.shop.domain.item.dto.ItemInfo;
import com.shoppingmall.shop.domain.item.entity.Item;
import com.shoppingmall.shop.domain.item.repository.ItemRepository;
import com.shoppingmall.shop.domain.member.entity.Member;
import com.shoppingmall.shop.domain.member.repository.MemberRepository;
import com.shoppingmall.shop.global.exception.MemberNotFoundException;
import com.shoppingmall.shop.global.exception.OutOfStockProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;


    // 상품 등록
    public Long saveItem(ItemInfo itemInfo, Long loginId) {

        Member findMember = validateMember(loginId);

        Item item = Item.createItem(itemInfo);

        itemRepository.save(item);

        return item.getItemId();
    }


    // 상품 조회
    public ItemInfo getItem(Long itemId) {

        Item item = itemRepository.findById(itemId).orElseThrow(OutOfStockProductException::new);

        return ItemInfo.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .itemDetail(item.getItemDetail())
                .stockCount(item.getStockCount())
                .build();
    }

    // 상품 수정
    public void updateItem(Long itemId, ItemInfo itemInfo, Long loginId) {

        Member findMember = validateMember(loginId);

        Item item = itemRepository.findById(itemId).orElseThrow(OutOfStockProductException::new);

        item.updateItem(itemInfo.getItemName(), itemInfo.getPrice());
    }

    // 상품 삭제
    public void deleteItem(Long itemId, Long loginId) {

        Member findMember = validateMember(loginId);
        Item findItem = validateItem(itemId);

        itemRepository.deleteById(itemId);
    }

    private Item validateItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(OutOfStockProductException::new);
    }

    private Member validateMember(Long loginId) {
        return memberRepository.findById(loginId).orElseThrow(MemberNotFoundException::new);
    }
}
