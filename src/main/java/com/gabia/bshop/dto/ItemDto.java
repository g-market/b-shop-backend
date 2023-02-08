package com.gabia.bshop.dto;

import java.time.LocalDateTime;

import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.enumtype.ItemStatus;

import lombok.Builder;

@Builder
public record ItemDto(
	Long id,

	OptionDto optionDto,

	ItemImageDto itemImageDto,

	CategoryDto categoryDto,

	String name,
	String description,
	int basePrice,
	ItemStatus itemStatus,
	LocalDateTime openAt
) {
}
