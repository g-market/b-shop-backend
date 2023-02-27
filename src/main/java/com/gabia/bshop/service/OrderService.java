package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.dto.request.OrderCreateRequestDto;
import com.gabia.bshop.dto.request.OrderInfoSearchRequest;
import com.gabia.bshop.dto.request.OrderUpdateStatusRequest;
import com.gabia.bshop.dto.response.OrderCreateResponseDto;
import com.gabia.bshop.dto.response.OrderInfoPageResponse;
import com.gabia.bshop.dto.response.OrderInfoSingleResponse;
import com.gabia.bshop.dto.response.OrderUpdateStatusResponse;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.ItemOption;
import com.gabia.bshop.entity.Order;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.gabia.bshop.entity.enumtype.OrderStatus;
import com.gabia.bshop.exception.BadRequestException;
import com.gabia.bshop.exception.ConflictException;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.OrderInfoMapper;
import com.gabia.bshop.mapper.OrderMapper;
import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.repository.ItemOptionRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrderRepository;
import com.gabia.bshop.security.MemberPayload;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ItemImageRepository itemImageRepository;
	private final ItemOptionRepository itemOptionRepository;

	@Transactional(readOnly = true)
	public OrderInfoPageResponse findOrdersPagination(final Long memberId, final Pageable pageable) {
		final List<Order> orderList = orderRepository.findByMemberIdPagination(memberId, pageable);
		final List<OrderItem> orderItemList = findOrderItemListByOrderList(orderList);
		final List<ItemImage> itemImagesWithItem = itemImageRepository.findWithItemByItemIds(
			orderItemList.stream().map(oi -> oi.getItem().getId()).collect(Collectors.toList()));

		return OrderInfoMapper.INSTANCE.orderInfoRelatedEntitiesToOrderInfoPageResponse(orderList,
			orderItemList,
			itemImagesWithItem);
	}

	@Transactional(readOnly = true)
	public OrderInfoSingleResponse findSingleOrderInfo(final MemberPayload memberPayload, final Long orderId) {
		//권한 확인
		if (memberPayload.isAdmin()) {
			findOrderById(orderId);
		} else {
			findOrderByIdAndMemberId(orderId, memberPayload.id());
		}

		final List<OrderItem> orderInfoList = orderItemRepository.findWithOrdersAndItemByOrderId(orderId);
		final List<String> thumbnailUrlList = itemImageRepository.findUrlByItemIds(orderInfoList.stream()
			.map(oi -> oi.getItem().getId())
			.collect(Collectors.toList()));
		return OrderInfoMapper.INSTANCE.orderInfoSingleDTOResponse(orderInfoList, thumbnailUrlList);
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

	public OrderCreateResponseDto purchase(final Long memberId, final OrderCreateRequestDto orderCreateRequestDto) {
		final Order order = OrderMapper.INSTANCE.ordersCreateDtoToEntity(memberId, orderCreateRequestDto);

		List<OrderItemDto> orderItemDtoList = orderCreateRequestDto.orderItemDtoList();
		final List<ItemOption> itemOptionList = itemOptionRepository.findByItemIdListAndIdListWithLock(
			orderItemDtoList);

		isEqualListSize(orderItemDtoList, itemOptionList);

		final List<OrderItem> orderItemList = new ArrayList<>();
		for (int i = 0; i < itemOptionList.size(); i++) {
			ItemOption itemOption = itemOptionList.get(i);
			int orderCount = orderItemDtoList.get(i).orderCount();

			validateItemStatus(itemOption);
			validateStockQuantity(itemOption, orderCount);

			orderItemList.add(OrderItem.createOrderItem(itemOption, order, orderCount));
		}

		order.createOrder(orderItemList);
		orderRepository.save(order);

		return OrderMapper.INSTANCE.ordersCreateResponseDto(order);
	}

	public void cancelOrder(final Long memberId, final Long orderId) {
		final Order order = findOrderByIdAndMemberIdWithLock(orderId, memberId);
		validateOrderStatus(order);
		order.cancelOrder();
	}

	public OrderUpdateStatusResponse updateOrderStatus(final OrderUpdateStatusRequest orderUpdateStatusRequest) {
		final Order order = findOrderById(orderUpdateStatusRequest.orderId());
		validateOrderStatus(order);
		order.updateOrderStatus(orderUpdateStatusRequest.status());

		return OrderMapper.INSTANCE.orderToOrderUpdateStatusResponse(order);
	}

	private Order findOrderById(final Long orderId) {
		return orderRepository.findById(orderId)
			.orElseThrow(() -> new NotFoundException(ORDER_NOT_FOUND_EXCEPTION, orderId));
	}

	private Order findOrderByIdAndMemberId(final Long orderId, final Long memberId) {
		return orderRepository.findByIdAndMemberId(orderId, memberId)
			.orElseThrow(() -> new NotFoundException(ORDER_NOT_FOUND_EXCEPTION, orderId));
	}

	private Order findOrderByIdAndMemberIdWithLock(final Long orderId, final Long memberId) {
		return orderRepository.findByIdAndMemberIdWithLock(orderId, memberId)
			.orElseThrow(() -> new NotFoundException(ORDER_NOT_FOUND_EXCEPTION, orderId));
	}

	private List<OrderItem> findOrderItemListByOrderList(final List<Order> orderList) {
		return orderItemRepository.findByOrderIdIn(orderList.stream()
			.map(order -> order.getId())
			.collect(Collectors.toList()));
	}

	private void validateItemStatus(final ItemOption itemOption) {
		if (itemOption.getItem().getItemStatus() != ItemStatus.PUBLIC) {
			throw new ConflictException(ITEM_STATUS_NOT_PUBLIC_EXCEPTION);
		}
	}

	private void validateStockQuantity(final ItemOption itemOption, final int orderCount) {
		final int restStock = itemOption.getStockQuantity() - orderCount;
		if (restStock < 0) {
			throw new ConflictException(ITEM_OPTION_OUT_OF_STOCK_EXCEPTION, itemOption.getStockQuantity());
		}
	}

	private void validateOrderStatus(final Order order) {
		if (order.getStatus() == OrderStatus.COMPLETED) {
			throw new ConflictException(ORDER_STATUS_ALREADY_COMPLETED_EXCEPTION);
		} else if (order.getStatus() == OrderStatus.CANCELLED) {
			throw new ConflictException(ORDER_STATUS_ALREADY_CANCELLED_EXCEPTION);
		}
	}

	private boolean isEqualListSize(final List<OrderItemDto> orderItemDtoList,
		final List<ItemOption> validItemOptionList) {
		if (orderItemDtoList.size() != validItemOptionList.size()) {
			throw new BadRequestException(INVALID_ITEM_OPTION_NOT_FOUND_EXCEPTION);
		}
		return true;
	}
}
