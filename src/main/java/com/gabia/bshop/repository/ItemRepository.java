package com.gabia.bshop.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gabia.bshop.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

	Page<Item> findAll(Pageable page);

}
