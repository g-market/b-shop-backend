<<<<<<<< HEAD:src/main/java/com/gabia/bshop/service/OrdersService.java
package com.gabia.bshop.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.response.OrderInfoPageResponse;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.Orders;
import com.gabia.bshop.mapper.OrderInfoMapper;
import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrdersRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrdersService {

	private final OrdersRepository ordersRepository;
	private final OrderItemRepository orderItemRepository;
	private final ItemImageRepository itemImageRepository;
	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public OrderInfoPageResponse findOrdersPagination(final Long memberId, final Pageable pageable) {
		memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException("해당하는 id의 회원이 존재하지 않습니다."));

		final List<Orders> orders = ordersRepository.findByMemberIdPagination(memberId, pageable);
		final List<OrderItem> orderItems = orderItemRepository.findByOrderIds(
			orders.stream().map(o -> o.getId()).collect(Collectors.toList()));
		final List<ItemImage> itemImagesWithItem = itemImageRepository.findWithItemByItemIds(
			orderItems.stream().map(oi -> oi.getItem().getId()).collect(Collectors.toList()));

		return OrderInfoMapper.INSTANCE.orderInfoRelatedEntitiesToOrderInfoPageResponse(orders, orderItems,
			itemImagesWithItem);
	}
}
========
>>>>>>>> 4b8cca2 (rename: order -> orders 로 rename 변경):src/main/java/com/gabia/bshop/service/OrderService.java
