package com.gabia.bshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabia.bshop.entity.ItemOption;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {

	ItemOption findByItem_Id(Long id);

	List<ItemOption> findAllByItem_id(Long id);

}
