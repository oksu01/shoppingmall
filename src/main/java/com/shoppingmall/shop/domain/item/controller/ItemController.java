package com.shoppingmall.shop.domain.item.controller;


import com.shoppingmall.shop.domain.item.dto.ItemInfo;
import com.shoppingmall.shop.domain.item.service.ItemService;
import com.shoppingmall.shop.global.annotation.LoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/new")
    public ResponseEntity<Void> createItem(@RequestBody ItemInfo itemInfo,
                                           @LoginId Long loginId) {

        Long itemId = itemService.saveItem(itemInfo, loginId);

        URI uri = URI.create("/items/" + itemId);

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{item-id}")
    public ResponseEntity<ItemInfo> getItem(@Positive @PathVariable Long itemId) {

        ItemInfo itemInfo = itemService.getItem(itemId);

        return ResponseEntity.ok(itemInfo);
    }

    @PatchMapping("{item-id}")
    public ResponseEntity<Void> updateItem(@PathVariable @Positive Long itemId,
                                           @RequestBody ItemInfo itemInfo,
                                           @LoginId Long loginId) {

        itemService.updateItem(itemId, itemInfo, loginId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{item-id}")
    public ResponseEntity<Void> deleteItem(@Positive @PathVariable Long itemId,
                                           @LoginId Long loginId) {

        itemService.deleteItem(itemId, loginId);

        return ResponseEntity.noContent().build();
    }

}
