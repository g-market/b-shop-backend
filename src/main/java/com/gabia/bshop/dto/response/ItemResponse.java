package com.gabia.bshop.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.dto.ItemImageDto;
import com.gabia.bshop.dto.ItemOptionDto;
import com.gabia.bshop.entity.enumtype.ItemStatus;

/**
 * Item 에 관련된 상세 정보
 * Item 조회 시 사용
 * Item entity
 * Category entity
 * Option list entity
 * ItemImage list entity
 **/
public record ItemResponse(
	Long id,
	List<ItemOptionDto> itemOptionDtoList,
	List<ItemImageDto> itemImageDtoList,
	CategoryDto categoryDto,
	String name,
	String description,
	int basePrice,
	ItemStatus itemStatus,
	LocalDateTime openAt) {

}
