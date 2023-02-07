package com.gabia.bshop.dto;

import com.gabia.bshop.entity.enumtype.OrderStatus;
import java.util.List;
import lombok.Builder;

@Builder
public record OrdersCreateRequestDto(
        Long memberId,
        List<OrdersDto> orderItems,
        OrderStatus status
) {}
