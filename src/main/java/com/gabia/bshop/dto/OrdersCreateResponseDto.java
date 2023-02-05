package com.gabia.bshop.dto;

import com.gabia.bshop.entity.enumtype.OrderStatus;
import java.util.List;
import lombok.Builder;

@Builder
public record OrdersCreateResponseDto(
        Long memberId,
        List<OrdersDto> itemList,
        OrderStatus status,
        long totalPrice
) {}
