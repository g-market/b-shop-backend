package com.gabia.bshop.dto;

import com.gabia.bshop.entity.enumtype.OrderStatus;
import java.util.List;
import lombok.Builder;

@Builder
public record OrdersCreateResponseDto(
        Long id,
        Long memberId,
        List<OrdersDto> orderItems,
        OrderStatus status,
        long totalPrice
) {}
