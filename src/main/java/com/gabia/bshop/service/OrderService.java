package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

import com.gabia.bshop.dto.request.OrderInfoSearchRequest;
import com.gabia.bshop.dto.response.OrderInfoSingleResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.request.OrderCreateRequestDto;
import com.gabia.bshop.dto.response.OrderCreateResponseDto;
import com.gabia.bshop.dto.response.OrderInfoPageResponse;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.ItemOption;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.entity.Order;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.OrderInfoMapper;
import com.gabia.bshop.mapper.OrderMapper;
import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.repository.ItemOptionRepository;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ItemRepository itemRepository;
	private final ItemImageRepository itemImageRepository;
	private final MemberRepository memberRepository;
	private final ItemOptionRepository itemOptionRepository;

	@Transactional(readOnly = true)
	public OrderInfoPageResponse findOrdersPagination(final Long memberId, final Pageable pageable) {
		findMemberById(memberId);

		final List<Order> orderList = orderRepository.findByMemberIdPagination(memberId, pageable);
		final List<OrderItem> orderItemList = findOrderItemListByOrderList(orderList);
		final List<ItemImage> itemImagesWithItem = itemImageRepository.findWithItemByItemIds(
			orderItemList.stream().map(oi -> oi.getItem().getId()).collect(Collectors.toList()));

		return OrderInfoMapper.INSTANCE.orderInfoRelatedEntitiesToOrderInfoPageResponse(orderList,
			orderItemList,
			itemImagesWithItem);
	}

    @Transactional(readOnly = true)
    public OrderInfoSingleResponse findSingleOrderInfo(final Long orderId) {
        final List<OrderItem> orderInfo = orderItemRepository.findWithOrdersAndItemByOrderId(orderId);
        final List<String> thumbnailUrls = itemImageRepository.findUrlByItemIds(orderInfo.stream()
                .map(oi -> oi.getItem().getId())
                .collect(Collectors.toList()));
        return OrderInfoMapper.INSTANCE.orderInfoSingleDTOResponse(orderInfo, thumbnailUrls);
    }

    @Transactional(readOnly = true)
    public OrderInfoPageResponse findAdminOrdersPagination(final OrderInfoSearchRequest orderInfoSearchRequest,
                                                           final Pageable pageable) {
        final List<Order> orderList = orderRepository.findAllByPeriodPagination(orderInfoSearchRequest.startAt(),
                orderInfoSearchRequest.endAt(), pageable);
        final List<OrderItem> orderItems = findOrderItemListByOrderList(orderList);
        final List<ItemImage> itemImagesWithItem = itemImageRepository.findWithItemByItemIds(orderItems.stream()
                .map(oi -> oi.getItem().getId())
                .collect(Collectors.toList()));

        return OrderInfoMapper.INSTANCE.orderInfoRelatedEntitiesToOrderInfoPageResponse(orderList, orderItems,
                itemImagesWithItem);
    }

	/**
	 * 주문 생성
	 */
	public OrderCreateResponseDto createOrder(final OrderCreateRequestDto orderCreateRequestDto) {
		findMemberById(orderCreateRequestDto.memberId());
		Order order = OrderMapper.INSTANCE.ordersCreateDtoToEntity(orderCreateRequestDto);

		List<OrderItem> orderItemEntityList = orderCreateRequestDto.orderItemDtoList()
			.stream()
			.map(oi -> {
				Item item = itemRepository.findById(oi.id()).get();
				ItemOption itemOption = itemOptionRepository.findByItem_Id(oi.id());
				return OrderItem.createOrderItem(item, itemOption, order, oi.orderCount());
			})
			.collect(Collectors.toList());

		order.createOrder(orderItemEntityList);

		orderRepository.save(order);

		return OrderMapper.INSTANCE.ordersCreateResponseDto(order);
	}

	/**
	 * 주문 취소(제거)
	 */
	public void cancelOrder(final Long id) {
		Order order = findOrderById(id);

		order.cancel();
	}

	private Member findMemberById(final Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND_EXCEPTION, memberId));
	}

	private Order findOrderById(final Long orderId) {
		return orderRepository.findById(orderId)
			.orElseThrow(() -> new NotFoundException(ORDER_NOT_FOUND_EXCEPTION, orderId));
	}

	private List<OrderItem> findOrderItemListByOrderList(final List<Order> orderList){
		return orderItemRepository.findByOrderIds(orderList.stream()
				.map(order -> order.getId())
				.collect(Collectors.toList()));
	}

}

