package com.gabia.bshop.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.dto.ItemImageDto;
import com.gabia.bshop.dto.OptionDto;
import com.gabia.bshop.entity.enumtype.ItemStatus;

public record ItemUpdateResponse(
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
