package com.gabia.bshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabia.bshop.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
