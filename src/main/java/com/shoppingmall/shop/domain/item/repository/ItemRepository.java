package com.shoppingmall.shop.domain.item.repository;

import com.shoppingmall.shop.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
