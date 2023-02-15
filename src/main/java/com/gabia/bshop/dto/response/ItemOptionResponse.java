package com.gabia.bshop.dto.response;

public record ItemOptionResponse(
	Long itemId,
	Long OptionId,
	String description,
	int optionPrice,
	int stockQuantity
){

}
