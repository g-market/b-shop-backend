package com.gabia.bshop.dto;

import lombok.Builder;

@Builder
public record CategoryDto(
	Long id,
	String name,
	boolean deleted
) {
}
