package com.gabia.bshop.dto;

import com.gabia.bshop.entity.enumtype.ItemStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CategoryDto(
        Long id,
        String name
) {}
