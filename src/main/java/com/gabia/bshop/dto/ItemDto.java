package com.gabia.bshop.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.enumtype.ItemStatus;

import lombok.Builder;

@Builder
public record ItemDto(
	Long id,

	List<OptionDto> optionDtoList,

	List<ItemImageDto> itemImageDtoList,

	CategoryDto categoryDto,

	String name,
	String description,
	int basePrice,
	ItemStatus itemStatus,
	LocalDateTime openAt
) {
}
