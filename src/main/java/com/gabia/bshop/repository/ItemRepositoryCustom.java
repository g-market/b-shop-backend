package com.gabia.bshop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gabia.bshop.dto.searchConditions.ItemSearchConditions;
import com.gabia.bshop.entity.Item;

public interface ItemRepositoryCustom {

	List<Integer> findItemYears();

	Page<Item> findItemListByItemSearchConditions(Pageable pageable, ItemSearchConditions itemSearchConditions);

	Page<Item> findItemListWithDeletedByItemSearchConditions(Pageable pageable,
		ItemSearchConditions itemSearchConditions);
}
