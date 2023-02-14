package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.dto.request.OrderCreateRequestDto;
import com.gabia.bshop.dto.response.OrderCreateResponseDto;
import com.gabia.bshop.dto.response.OrderInfoPageResponse;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.ItemOption;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.entity.Order;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.gabia.bshop.entity.enumtype.OrderStatus;
import com.gabia.bshop.exception.ConflictException;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.OrderInfoMapper;
import com.gabia.bshop.mapper.OrderMapper;
import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.repository.ItemOptionRepository;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrderRepository;

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
	private final ItemOptionRepository itemOptionRepository;

	@Transactional(readOnly = true)
	public OrderInfoPageResponse findOrdersPagination(final Long memberId, final Pageable pageable) {
		findMemberById(memberId);

		final List<Order> order = orderRepository.findByMemberIdPagination(memberId, pageable);
		final List<OrderItem> orderItemList = orderItemRepository.findByOrderIds(
			order.stream().map(o -> o.getId()).collect(Collectors.toList()));
		final List<ItemImage> itemImagesWithItem = itemImageRepository.findWithItemByItemIds(
			orderItemList.stream().map(oi -> oi.getItem().getId()).collect(Collectors.toList()));

		return OrderInfoMapper.INSTANCE.orderInfoRelatedEntitiesToOrderInfoPageResponse(order, orderItemList,
			itemImagesWithItem);
	}

	//TODO: 인증 로직 추가 필요
	public OrderCreateResponseDto createOrder(final OrderCreateRequestDto orderCreateRequestDto) {
		findMemberById(orderCreateRequestDto.memberId());
		Order order = OrderMapper.INSTANCE.ordersCreateDtoToEntity(orderCreateRequestDto);

		//DB에서 OptionItem 값 한번에 조회
		List<ItemOption> findAllItemOptionList = itemOptionRepository.findWithOptionAndItemById(
			orderCreateRequestDto.orderItemDtoList().stream().map(OrderItemDto::itemId).collect(Collectors.toList()),
			orderCreateRequestDto.orderItemDtoList().stream().map(OrderItemDto::optionId).collect(Collectors.toList())
		);

		//유효한 ItemOption값 인지 검사
		List<OrderItemDto> validItemOptionList = orderCreateRequestDto.orderItemDtoList().stream()
			.filter(oi -> findAllItemOptionList.stream().anyMatch(oi::equalsIds))
			.toList();

		//요청 List와 검증한 List size가 일치하지 않다면
		if (orderCreateRequestDto.orderItemDtoList().size() != validItemOptionList.size()) {
			throw new ConflictException(ITEM_ITEMOPTION_NOT_FOUND_EXCEPTION);
		}

		List<OrderItem> orderItemList = validItemOptionList.stream().map(oi -> {
			ItemOption itemOption = findAllItemOptionList.stream()
				.filter(oi::equalsIds)
				.findFirst()
				.orElseThrow();
			validateItemStatus(itemOption);
			validateStockQuantity(itemOption, oi.orderCount());

			return OrderItem.createOrderItem(itemOption, order, oi.orderCount());
		}).toList();

		order.createOrder(orderItemList);
		orderRepository.save(order);

		return OrderMapper.INSTANCE.ordersCreateResponseDto(order);
	}

	public void cancelOrder(final Long id) {
		Order order = findOrderById(id);

		validateOrderStatus(order);
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

	public void validateItemStatus(ItemOption itemOption) {
		if (itemOption.getItem().getItemStatus() != ItemStatus.PUBLIC) {
			//TODO: 어떤 아이템이 판매되지 않는지 RETURN
			throw new ConflictException(ITEM_STATUS_NOT_PUBLIC_EXCEPTION);
		}
	}

	public void validateStockQuantity(ItemOption itemOption, int orderCount) {
		int restStock = itemOption.getStockQuantity() - orderCount;
		if (restStock <= 0) {
			throw new ConflictException(ITEM_OPTION_OUT_OF_STOCK_EXCEPTION, itemOption.getStockQuantity());
		}
	}

	public void validateOrderStatus(Order order) {
		if (order.getStatus() == OrderStatus.COMPLETED) {
			throw new IllegalStateException("상품의 상태가 완료된 상태입니다.");
		} else if (order.getStatus() == OrderStatus.CANCELLED) {
			throw new IllegalStateException("상품이 이미 취소된 상태입니다.");
		}
	}
}

