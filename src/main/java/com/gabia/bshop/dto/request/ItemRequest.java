package com.gabia.bshop.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.dto.ItemImageDto;
import com.gabia.bshop.dto.ItemOptionDto;
import com.gabia.bshop.entity.enumtype.ItemStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ItemRequest(
	List<ItemOptionDto> itemOptionDtoList,
	List<ItemImageDto> itemImageDtoList,
	CategoryDto categoryDto,
	String name,
	String description,
	int basePrice,
	ItemStatus itemStatus,
	LocalDateTime openAt
) {
}
