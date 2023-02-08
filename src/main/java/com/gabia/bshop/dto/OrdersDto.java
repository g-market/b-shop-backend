package com.gabia.bshop.dto;

import lombok.Builder;

@Builder
public record OrdersDto(
        Long id,
        Long optionId,
        int orderCount
) {}
