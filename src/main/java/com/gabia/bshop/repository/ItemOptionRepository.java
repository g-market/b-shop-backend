package com.gabia.bshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabia.bshop.entity.ItemOption;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long>, ItemOptionRepositoryCustom {

	boolean existsByItem_IdAndIdAndStockQuantityIsGreaterThanEqual(Long itemId, Long itemOptionId, int stockQuantity);
}
