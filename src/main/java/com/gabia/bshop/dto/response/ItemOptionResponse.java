package com.gabia.bshop.dto.response;

import lombok.Builder;

@Builder
public record ItemOptionResponse(
	Long itemId,
	Long optionId,
	String description,
	int optionPrice,
	int stockQuantity
) {
}
