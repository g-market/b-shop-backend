package com.gabia.bshop.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import com.gabia.bshop.dto.CategoryDto;
import com.gabia.bshop.dto.ItemImageDto;
import com.gabia.bshop.entity.enumtype.ItemStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record ItemChangeRequest(
	@NotNull(message = "상품 ID 는 필수 값입니다.")
	Long itemId,
	CategoryDto categoryDto,
	String name,
	String description,

	@PositiveOrZero(message = "가격은 0원 이상입니다.")
	Integer basePrice,
	ItemStatus itemStatus,
	LocalDateTime openAt
) {
}
