package com.gabia.bshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gabia.bshop.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

	@Query("""
		select i
		from Item i
		where i.deleted = true
		""")
	Page<Item> findAll(Pageable page);
}
