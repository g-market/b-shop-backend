package com.gabia.bshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gabia.bshop.entity.ItemOption;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {

	ItemOption findByItem_Id(Long optionId);

	List<ItemOption> findAllByItem_id(Long optionId);

	void deleteAllByItem_Id(Long optionId);

	@Query("""
		select io from ItemOption io
		join fetch io.item
		where io.item.id in :itemIdList
		and io.id in :itemOptionIdList
		""")
	List<ItemOption> findWithItemByItemIdsAndItemOptionIds(List<Long> itemIdList, List<Long> itemOptionIdList);

	Optional<ItemOption> findByIdAndItemId(Long optionId, Long itemId);
	boolean existsByItem_IdAndIdAndStockQuantityIsGreaterThanEqual(Long itemId, Long itemOptionId, int stockQuantity);
}
