package com.gabia.bshop.dto.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CategoryUpdateRequest(
	Long id,
	@Length(max = 255, message = "255자 이내로 입력해주세요.")
	@NotBlank(message = "name을 입력해주세요.")
	String name
) {
}
