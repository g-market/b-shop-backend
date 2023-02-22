package com.gabia.bshop.dto;

import lombok.Builder;

@Builder
public record ItemOptionDto(
	Long id,
	String description,
	int optionPrice,
	int stockQuantity
) {
}
