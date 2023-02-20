package com.gabia.bshop.dto.response;

public record ItemOptionResponse(
	Long itemId,
	Long optionId,
	String description,
	int optionPrice,
	int stockQuantity
) {

}
