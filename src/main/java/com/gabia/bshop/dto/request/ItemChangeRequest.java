package com.gabia.bshop.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.dto.ItemImageDto;
import com.gabia.bshop.entity.enumtype.ItemStatus;

import lombok.Builder;

@Builder
public record ItemChangeRequest(
	CategoryDto categoryDto,
	String name,
	String description,
	int basePrice,
	ItemStatus itemStatus,
	LocalDateTime openAt
) {
}
