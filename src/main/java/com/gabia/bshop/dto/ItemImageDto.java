package com.gabia.bshop.dto;

import lombok.Builder;

@Builder
public record ItemImageDto(
	Long id,
	String url) {
}
