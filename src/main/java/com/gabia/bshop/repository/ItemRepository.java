package com.gabia.bshop.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gabia.bshop.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
	@Query("select i from Item i where i.deleted = true")
	Page<Item> findAll(Pageable page);

	@Query("select i from Item i join fetch i.category join fetch i.itemImageList join fetch i.optionsList where i.id = :id")
	Optional<Item> findWithCategoryAndImagesAndOptionsById(Long id);
}
