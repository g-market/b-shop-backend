package com.gabia.bshop.repository;

import com.gabia.bshop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
