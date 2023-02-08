package com.gabia.bshop.dto;

public record OptionDto(
	Long id,
	String description,
	int optionPrice,
	int stockQuantity
) {
}
