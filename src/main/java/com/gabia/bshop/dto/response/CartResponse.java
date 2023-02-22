package com.gabia.bshop.dto.response;

import lombok.Builder;

@Builder
public record CartResponse(
	Long itemId,
	Long itemOptionId,
	int orderCount,
	String name,
	int basePrice,
	int optionPrice,
	String category,
	String thumbnailUrl
) {
}
