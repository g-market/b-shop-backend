package com.gabia.bshop.dto.request;

import java.time.LocalDateTime;
import java.util.List;

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
	@NotNull(message = "categoryId 는 필수 입니다")
	Long categoryId,

	@NotNull(message = "name 을 입력하세요.")
	String name,

	@NotNull(message = "description 을 입력하세요")
	String description,

	@PositiveOrZero(message = "basePrice 는 0 이상입니다.")
	@NotNull
	Integer basePrice,
	ItemStatus itemStatus,

	@PositiveOrZero(message = "year 는 0 이상입니다.")
	@NotNull
	Integer year,

	LocalDateTime openAt
) {
}
