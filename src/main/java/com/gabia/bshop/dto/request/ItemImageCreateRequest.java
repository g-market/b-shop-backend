package com.gabia.bshop.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record ItemImageCreateRequest(
	@NotNull(message = "itemId 는 필수 값 입니다.")
	Long itemId,

	@NotNull(message = "urlList 는 필수 값 입니다.")
	List<String> urlList
) {
}
