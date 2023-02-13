package com.gabia.bshop.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.request.OrderCreateRequestDto;
import com.gabia.bshop.dto.response.OrderCreateResponseDto;
import com.gabia.bshop.dto.response.OrderInfoPageResponse;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.Options;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.Orders;
import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.gabia.bshop.entity.enumtype.OrderStatus;
import com.gabia.bshop.exception.OutOfStockException;
import com.gabia.bshop.mapper.OrderInfoMapper;
import com.gabia.bshop.mapper.OrderMapper;
import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.OptionsRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrderRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ItemImageRepository itemImageRepository;
	private final MemberRepository memberRepository;
	private final OptionsRepository optionsRepository;

	@Transactional(readOnly = true)
	public OrderInfoPageResponse findOrdersPagination(final Long memberId, final Pageable pageable) {
		memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("해당하는 id의 회원이 존재하지 않습니다."));

		final List<Orders> orders = orderRepository.findByMemberIdPagination(memberId, pageable);
		final List<OrderItem> orderItemList = orderItemRepository.findByOrderIds(
			orders.stream().map(o -> o.getId()).collect(Collectors.toList()));
		final List<ItemImage> itemImagesWithItem = itemImageRepository.findWithItemByItemIds(
			orderItemList.stream().map(oi -> oi.getItem().getId()).collect(Collectors.toList()));

		return OrderInfoMapper.INSTANCE.orderInfoRelatedEntitiesToOrderInfoPageResponse(orders, orderItemList,
			itemImagesWithItem);
	}

	public OrderCreateResponseDto createOrder(final OrderCreateRequestDto orderCreateRequestDto) {

		memberRepository.findById(orderCreateRequestDto.memberId()).orElseThrow(EntityNotFoundException::new);
		Orders orders = OrderMapper.INSTANCE.ordersCreateDtoToEntity(orderCreateRequestDto);

		// TODO: 하나의 item에 여러 option으로 주문할 경우 Query 성능 개선 검토
		List<OrderItem> orderItemList = orderCreateRequestDto.orderItemDtoList().stream().map(oi -> {
			Options options = optionsRepository.findWithOptionAndItemById(oi.optionId(), oi.itemId())
				.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품 옵션 ID 입니다."));
			validateItemStatus(options);
			validateStockQuantity(options, oi.orderCount());

			return OrderItem.createOrderItem(options, orders, oi.orderCount());
		}).collect(Collectors.toList());

		orders.createOrder(orderItemList);

		orderRepository.save(orders);

		return OrderMapper.INSTANCE.ordersCreateResponseDto(orders);
	}

	public void cancelOrder(final Long id) {
		Orders orders = orderRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문 ID 입니다."));

		validateOrderStatus(orders);
		orders.cancel();
	}

	public void validateItemStatus(Options options) {
		if (options.getItem().getItemStatus() != ItemStatus.PUBLIC) {
			throw new OutOfStockException("현재 판매하지 않는 상품 ID 입니다.");
		}
	}

	public void validateStockQuantity(Options options, int orderCount) {
		int restStock = options.getStockQuantity() - orderCount;
		if (restStock <= 0) {
			throw new OutOfStockException("상품의 재고가 부족합니다.");
		}
	}

	public void validateOrderStatus(Orders orders) {
		if (orders.getStatus() == OrderStatus.COMPLETED) {
			throw new IllegalStateException("상품의 상태가 완료된 상태입니다.");
		} else if (orders.getStatus() == OrderStatus.CANCELLED) {
			throw new IllegalStateException("상품이 이미 취소된 상태입니다.");
		}
	}
}

