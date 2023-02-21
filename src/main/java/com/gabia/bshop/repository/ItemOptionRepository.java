package com.gabia.bshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gabia.bshop.entity.ItemOption;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {

	@Query("""
		select io from ItemOption io
		join fetch io.item
		where io.item.id in :itemIdList
		and io.id in :itemOptionIdList
		""")
	List<ItemOption> findWithItemByItemIdsAndItemOptionIds(List<Long> itemIdList, List<Long> itemOptionIdList);
}
