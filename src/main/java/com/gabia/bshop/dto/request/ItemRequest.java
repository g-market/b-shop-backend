package com.gabia.bshop.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.gabia.bshop.dto.ItemImageDto;
import com.gabia.bshop.dto.ItemOptionDto;
import com.gabia.bshop.dto.validator.ByteSize;
import com.gabia.bshop.entity.enumtype.ItemStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ItemRequest(
	@Valid
	@Size(max = 100, message = "ItemOption 이 허용된 수 이상입니다. (최대 100개)")
	List<ItemOptionDto> itemOptionDtoList,

	@Valid
	List<ItemImageDto> itemImageDtoList,
	@NotNull(message = "categoryId 는 필수 입니다")
	Long categoryId,

	@Length(max = 255, message = "255자 이내로 입력해주세요.")
	@NotNull(message = "name 을 입력하세요.")
	String name,

	@NotNull(message = "description 을 입력하세요")
	@ByteSize(max = 64L, message = "description 의 크기가 허용된 크기 이상입니다.(64 Byte).")
	String description,

	@PositiveOrZero(message = "basePrice 는 0 이상입니다.")
	int basePrice,
	ItemStatus itemStatus,

	@PositiveOrZero(message = "year 는 0 이상입니다.")
	int year,

	LocalDateTime openAt
) {
}
