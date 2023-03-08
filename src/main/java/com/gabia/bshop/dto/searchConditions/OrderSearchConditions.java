package com.gabia.bshop.dto.searchConditions;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

public record OrderSearchConditions(

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime startDate,

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime endDate
) {
}
