package com.gabia.bshop.dto;

import com.gabia.bshop.entity.enumtype.ItemStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ItemDto(
        Long id,
        CategoryDto categoryDto,
        String name,
        String description,
        int basePrice,
        ItemStatus itemStatus,
        LocalDateTime openAt,
        boolean deleted
) {}
