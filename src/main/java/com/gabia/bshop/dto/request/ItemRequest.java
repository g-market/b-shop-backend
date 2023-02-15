package com.gabia.bshop.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.dto.ItemImageDto;
import com.gabia.bshop.dto.ItemOptionDto;
import com.gabia.bshop.entity.enumtype.ItemStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record ItemRequest(
	List<ItemOptionDto> itemOptionDtoList,
	List<ItemImageDto> itemImageDtoList,
	@NotNull
	CategoryDto categoryDto,

	@NotNull(message = "상품의 이름을 입력하세요.")
	String name,

	@NotNull(message = "상품 설명을 입력하세요")
	String description,
	@PositiveOrZero(message = "가격은 0원 이상입니다.")
	@NotNull
	Integer basePrice,

	ItemStatus itemStatus,
	LocalDateTime openAt
) {
}
