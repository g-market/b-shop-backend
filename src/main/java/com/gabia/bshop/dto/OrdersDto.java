package com.gabia.bshop.dto;

import com.gabia.bshop.entity.enumtype.OrderStatus;

public record OrdersDto(
        Long id,
        MemberDto memberDto,
        OrderStatus orderStatus,
        long totalPrice
) {}
