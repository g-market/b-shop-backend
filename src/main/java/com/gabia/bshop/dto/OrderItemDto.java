package com.gabia.bshop.dto;

import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record OrderItemDto(
	Long itemId,
	Long optionId,
	@Positive(message = "주문량은 양수만 허용합니다.")
	int orderCount
) {
}
