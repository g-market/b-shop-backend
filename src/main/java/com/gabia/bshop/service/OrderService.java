package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.dto.request.OrderCreateRequest;
import com.gabia.bshop.dto.request.OrderUpdateStatusRequest;
import com.gabia.bshop.dto.response.OrderCreateResponse;
import com.gabia.bshop.dto.response.OrderInfoPageResponse;
import com.gabia.bshop.dto.response.OrderInfoResponse;
import com.gabia.bshop.dto.response.OrderUpdateStatusResponse;
import com.gabia.bshop.dto.searchConditions.OrderSearchConditions;
import com.gabia.bshop.entity.ItemOption;
import com.gabia.bshop.entity.Order;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.gabia.bshop.entity.enumtype.OrderStatus;
import com.gabia.bshop.exception.BadRequestException;
import com.gabia.bshop.exception.ConflictException;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.OrderMapper;
import com.gabia.bshop.repository.ItemOptionRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrderRepository;
import com.gabia.bshop.security.MemberPayload;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ItemOptionRepository itemOptionRepository;

	public Page<OrderInfoPageResponse> findOrderInfoList(final Pageable pageable, final Long memberId,
		final OrderSearchConditions orderSearchConditions) {
		return orderRepository.findAllBySearchConditions(pageable, orderSearchConditions,
			memberId).map(OrderMapper.INSTANCE::orderToOrderInfoPageResponse);
	}

	public OrderInfoResponse findOrderInfo(final MemberPayload memberPayload, final Long orderId) {
		//권한 확인
		if (memberPayload.isAdmin()) {
			findOrderById(orderId);
		} else {
			findOrderByIdAndMemberId(orderId, memberPayload.id());
		}

		final List<OrderItem> orderInfoList = orderItemRepository.findWithOrderAndItemByOrderId(orderId);
		return OrderMapper.INSTANCE.orderItemListToOrderInfoResponse(orderInfoList);
	}

	public Page<OrderInfoPageResponse> findAllOrderInfoList(final OrderSearchConditions orderSearchConditions,
		final Pageable pageable) {
		return orderRepository.findAllBySearchConditions(pageable, orderSearchConditions, null)
			.map(OrderMapper.INSTANCE::orderToOrderInfoPageResponse);
	}

	@Transactional
	public OrderCreateResponse createOrder(final Long memberId, final OrderCreateRequest orderCreateRequest) {
		final Order order = OrderMapper.INSTANCE.orderCreateRequestToEntity(memberId, orderCreateRequest);

		final List<OrderItemDto> orderItemDtoList = orderCreateRequest.orderItemDtoList();
		final List<ItemOption> itemOptionList = itemOptionRepository.findByItemIdListAndIdListWithLock(
			orderItemDtoList);

		isEqualListSize(orderItemDtoList, itemOptionList);

		final List<OrderItem> orderItemList = new ArrayList<>();
		for (int i = 0; i < itemOptionList.size(); i++) {
			final ItemOption itemOption = itemOptionList.get(i);
			int orderCount = orderItemDtoList.get(i).orderCount();

			validateItemStatus(itemOption);
			validateStockQuantity(itemOption, orderCount);

			orderItemList.add(OrderItem.createOrderItem(itemOption, order, orderCount));
		}

		order.createOrder(orderItemList);
		orderRepository.save(order);

		return OrderMapper.INSTANCE.orderCreateResponseToDto(order);
	}

	@Transactional
	public void cancelOrder(final Long memberId, final Long orderId) {
		final Order order = findOrderByIdAndMemberId(orderId, memberId);
		validateCancelOrderStatus(order);
		itemOptionRepository.findByItemIdListAndIdListInOrderItemListWithLock(order.getOrderItemList());
		order.cancelOrder();
	}

	@Transactional
	public OrderUpdateStatusResponse updateOrderStatus(final OrderUpdateStatusRequest orderUpdateStatusRequest) {
		final Order order = findOrderById(orderUpdateStatusRequest.orderId());
		validateUpdateOrderStatus(orderUpdateStatusRequest, order);
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

	private void validateItemStatus(final ItemOption itemOption) {
		if (itemOption.getItem().getItemStatus() != ItemStatus.PUBLIC) {
			throw new ConflictException(ITEM_STATUS_NOT_PUBLIC_EXCEPTION);
		}
	}

	private void validateStockQuantity(final ItemOption itemOption, final int orderCount) {
		final int restStock = itemOption.getStockQuantity() - orderCount;
		if (restStock < 0) {
			throw new ConflictException(ITEM_OPTION_OUT_OF_STOCK_EXCEPTION, itemOption.getId(),
				itemOption.getStockQuantity());
		}
	}

	private void validateCancelOrderStatus(final Order order) {
		if (order.getStatus() != OrderStatus.ACCEPTED) {
			throw new ConflictException(ORDER_STATUS_ALREADY_CANCELLED_EXCEPTION, order.getStatus());
		}
	}

	private void validateUpdateOrderStatus(final OrderUpdateStatusRequest orderUpdateStatusRequest, final Order order) {
		if (orderUpdateStatusRequest.status() == order.getStatus()) {
			throw new ConflictException(ORDER_STATUS_ALREADY_UPDATED_EXCEPTION, order.getStatus());
		}
	}

	private void isEqualListSize(final List<OrderItemDto> orderItemDtoList,
		final List<ItemOption> validItemOptionList) {
		if (orderItemDtoList.size() != validItemOptionList.size()) {
			throw new BadRequestException(INVALID_ITEM_OPTION_NOT_FOUND_EXCEPTION);
		}
	}
}
