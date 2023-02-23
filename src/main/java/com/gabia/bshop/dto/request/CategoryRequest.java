package com.gabia.bshop.dto.request;

import jakarta.validation.constraints.NotNull;

public record CategoryRequest(
	@NotNull(message = "name을 입력해주세요.")
	String name
) {
}
