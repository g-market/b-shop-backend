package com.gabia.bshop.service;

import com.gabia.bshop.dto.response.OrderInfoPageResponse;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.Orders;
import com.gabia.bshop.mapper.OrderInfoMapper;
import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemImageRepository itemImageRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public OrderInfoPageResponse findOrdersPagination(Long memberId, Pageable pageable) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("해당하는 id의 회원이 존재하지 않습니다."));

        List<Orders> orders = orderRepository.findByMemberIdPagination(memberId, pageable);
        List<OrderItem> orderItems = orderItemRepository.findByOrderIds(orders.stream().map(o -> o.getId()).collect(Collectors.toList()));
        List<ItemImage> itemImagesWithItem = itemImageRepository.findWithItemByItemIds(orderItems.stream().map(oi -> oi.getItem().getId()).collect(Collectors.toList()));
        OrderInfoPageResponse orderInfoPageResponse = OrderInfoMapper.INSTANCE.orderInfoRelatedEntitiesToOrderInfoPageResponse(
                orders, orderItems, itemImagesWithItem);
        System.out.println("orderInfoPageResponse = " + orderInfoPageResponse);
        return orderInfoPageResponse;
    }
}
