package com.gabia.bshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

	Page<Item> findAll(Pageable page);

	@Query(value = "select * from item where id = :itemId", nativeQuery = true)
	Optional<Item> findIdWithDeleted(Long itemId);

	@Query(value = "select * from item", nativeQuery = true)
	Page<Item> findAllWithDeleted(Pageable page);

	Page<Item> findByCategory(Category category, Pageable page);

	List<Item> findAllByCategoryId(Long categoryId);
}
