package com.gabia.bshop.dto;

import com.gabia.bshop.entity.enumtype.OrderStatus;
import java.util.List;

public record OrdersDto(Long id,
                        MemberDto memberDto,
                        //List<ItemDto> itemDtoList,
                        OrderStatus orderStatus,
                        long totalPrice
){}
