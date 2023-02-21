package com.gabia.bshop.dto.request;

import java.time.LocalDateTime;

import com.gabia.bshop.entity.enumtype.ItemStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record ItemChangeRequest(
	@NotNull(message = "itemId 는 필수 값입니다.")
	Long itemId,
	Long categoryId,
	String name,
	String description,

	@PositiveOrZero(message = "basePrice 는 0 이상입니다.")
	Integer basePrice,
	ItemStatus itemStatus,

	@PositiveOrZero(message = "year 는 0 이상입니다.")
	Integer year,

	LocalDateTime openAt
) {
}
