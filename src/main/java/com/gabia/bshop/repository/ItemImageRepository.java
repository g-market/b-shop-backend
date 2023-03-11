package com.gabia.bshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabia.bshop.entity.ItemImage;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

	List<ItemImage> findAllByItemId(Long itemId);

	Optional<ItemImage> findByIdAndItemId(Long imageId, Long itemId);
}
