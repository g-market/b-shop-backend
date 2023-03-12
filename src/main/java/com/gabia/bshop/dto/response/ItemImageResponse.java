package com.gabia.bshop.dto.response;

import lombok.Builder;

@Builder
public record ItemImageResponse(
	Long imageId,
	String imageUrl
) {
}
