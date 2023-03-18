package com.gabia.bshop.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.dto.ItemImageDto;
import com.gabia.bshop.dto.ItemOptionDto;
import com.gabia.bshop.entity.enumtype.ItemStatus;

import lombok.Builder;

/**
 * Item 에 관련된 상세 정보
 * Item 조회 시 사용
 * Item entity
 * Category entity
 * Option list entity
 * ItemImage list entity
 **/
@Builder
public record ItemResponse(
	Long itemId,
	List<ItemOptionDto> itemOptionDtoList,
	List<ItemImageDto> itemImageDtoList,
	CategoryDto categoryDto,
	String name,
	String description,
	int basePrice,
	String thumbnail,
	ItemStatus itemStatus,
	Integer year,
	LocalDateTime openAt) {
}
