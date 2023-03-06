package com.gabia.bshop.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record ItemOptionDto(
	Long id,

	@Length(max = 255, message = "255자 이내로 입력해주세요.")
	@NotBlank(message = "itemOption 의 description 은 필수 값입니다.")
	String description,

	int optionPrice,

	@PositiveOrZero(message = "stockQuantity 는 0 이상입니다.")
	int stockQuantity
) {
}
