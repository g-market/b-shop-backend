package com.gabia.bshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gabia.bshop.entity.ItemOption;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {

	@Query("""
		select o from Options o
		join fetch o.item
		where o.item.id = :itemId
		and o.id = :optionId
		""")
	Optional<ItemOption> findWithOptionAndItemById(Long itemId, Long optionId);
}
