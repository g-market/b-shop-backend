package com.gabia.bshop.dto;

import lombok.Builder;

@Builder
public record OrderItemDto(
        Long id,
        int orderCount
) {}
