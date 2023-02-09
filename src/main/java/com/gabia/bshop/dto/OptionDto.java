package com.gabia.bshop.dto;

import lombok.Builder;

@Builder
public record OptionDto(
	Long id,
	String description,
	int optionPrice,
	int stockQuantity
) {
}
