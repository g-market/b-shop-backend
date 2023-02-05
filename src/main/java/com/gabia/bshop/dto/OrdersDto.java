package com.gabia.bshop.dto;

import lombok.Builder;

@Builder
public record OrdersDto(
        Long id,
        int orderCount
) {}
