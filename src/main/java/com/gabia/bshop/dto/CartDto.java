package com.gabia.bshop.dto;

public record CartDto(
	Long itemId,
	Long itemOptionId,
	int orderCount
) {
}
