package com.gabia.bshop.dto.searchConditions;

public record ItemSearchConditions(
	String categoryName,
	String itemName,
	Integer year
) {
}
