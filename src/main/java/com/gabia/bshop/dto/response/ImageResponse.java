package com.gabia.bshop.dto.response;

import lombok.Builder;

@Builder
public record ImageResponse(
	String fileName,
	String url
) {
}
