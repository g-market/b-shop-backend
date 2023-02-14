package com.gabia.bshop.dto;

import com.gabia.bshop.entity.ItemOption;

import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record OrderItemDto(
	Long itemId,
	Long optionId,
	@Positive(message = "주문량은 양수만 허용합니다.")
	int orderCount
) {
	public boolean equalsIds(ItemOption itemOption) {
		return itemOption.getItem().getId().equals(this.itemId)
			&& itemOption.getId().equals(this.optionId);
	}
}
