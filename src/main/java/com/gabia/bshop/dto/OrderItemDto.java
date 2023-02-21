package com.gabia.bshop.dto;

import com.gabia.bshop.entity.ItemOption;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record OrderItemDto(
	@NotNull(message = "아이템 ID는 필수값입니다.")
	Long itemId,
	@NotNull(message = "옵션 ID는 필수값입니다.")
	Long itemOptionId,
	@NotNull(message = "주문량은 필수값입니다.")
	@Positive(message = "주문량은 양수만 허용합니다.")
	int orderCount
) {
	public boolean equalsIds(final ItemOption itemOption) {
		return itemOption.getItem().getId().equals(this.itemId)
			&& itemOption.getId().equals(this.itemOptionId);
	}
}
