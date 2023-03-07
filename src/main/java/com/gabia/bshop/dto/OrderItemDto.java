package com.gabia.bshop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record OrderItemDto(
	@NotNull(message = "아이템 ID는 필수값입니다.")
	Long itemId,
	@NotNull(message = "옵션 ID는 필수값입니다.")
	Long itemOptionId,
	@Positive(message = "주문량은 양수만 허용합니다.")
	int orderCount
) implements ItemIdAndItemOptionIdAble {
}
