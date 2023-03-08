package com.gabia.bshop.dto.request;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;

import com.gabia.bshop.dto.validator.ByteSize;
import com.gabia.bshop.entity.enumtype.ItemStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record ItemUpdateRequest(
	@NotNull(message = "itemId 는 필수 값입니다.")
	Long itemId,
	Long categoryId,

	@Length(max = 255, message = "255자 이내로 입력해주세요.")
	String name,

	@ByteSize(max = 64L, message = "itemOptionDescription 의 크기가 허용된 크기 이상입니다.(64 Byte).")
	String description,

	@PositiveOrZero(message = "basePrice 는 0 이상입니다.")
	Integer basePrice,
	ItemStatus itemStatus,

	@PositiveOrZero(message = "year 는 0 이상입니다.")
	Integer year,

	LocalDateTime openAt
) {
}
