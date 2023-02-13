package com.gabia.bshop.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.dto.ItemImageDto;
import com.gabia.bshop.dto.ItemOptionDto;
import com.gabia.bshop.entity.enumtype.ItemStatus;

public record ItemChangeResponse(
	Long id,
	CategoryDto categoryDto,
	String name,
	String description,
	int basePrice,
	ItemStatus itemStatus,
	LocalDateTime openAt
) {
}
