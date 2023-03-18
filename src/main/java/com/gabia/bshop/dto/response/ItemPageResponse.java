package com.gabia.bshop.dto.response;

import java.time.LocalDateTime;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.entity.enumtype.ItemStatus;

public record ItemPageResponse(
	Long itemId,
	CategoryDto categoryDto,
	String name,
	String description,
	int basePrice,
	String thumbnail,
	ItemStatus itemStatus,
	Integer year,
	LocalDateTime openAt
) {
}
