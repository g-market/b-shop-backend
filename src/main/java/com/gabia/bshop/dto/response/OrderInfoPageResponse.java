package com.gabia.bshop.dto.response;

import com.gabia.bshop.entity.enumtype.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public record OrderInfoPageResponse(int resultCount, List<OrderInfo> orderInfos) {

    public record OrderInfo(long orderId,
                            String thumbnailImage,
                            String representativeName,
                            int itemTotalCount,
                            OrderStatus orderStatus,
                            long totalPrice,
                            LocalDateTime createdAt) {

    }
}
