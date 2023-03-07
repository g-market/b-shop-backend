package com.gabia.bshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.gabia.bshop.entity.ItemOption;

import jakarta.persistence.LockModeType;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long>, ItemOptionRepositoryCustom {

	List<ItemOption> findAllByItemId(Long itemId);

	@Query("""
		select io from ItemOption io
		join fetch io.item
		where io.item.id in :itemIdList
		and io.id in :itemOptionIdList
		""")
	List<ItemOption> findWithItemByItemIdsAndItemOptionIds(List<Long> itemIdList, List<Long> itemOptionIdList);

	Optional<ItemOption> findByIdAndItemId(Long itemOptionId, Long itemId);

	@Query("""
		select io from ItemOption io
		where io.id in :itemOptionId
		and io.item.id in :itemId
		""")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<ItemOption> findByIdAndItemIdWithLock(Long itemOptionId, Long itemId);

	boolean existsByItem_IdAndIdAndStockQuantityIsGreaterThanEqual(Long itemId, Long itemOptionId, int stockQuantity);

	List<ItemOption> findAllByOrderByIdAsc();
}
