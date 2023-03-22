package com.gabia.bshop.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.dto.request.OrderCreateRequest;
import com.gabia.bshop.dto.response.OrderCreateResponse;
import com.gabia.bshop.entity.Order;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.mapper.OrderMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderFacadeService {

	private final RedissonClient redissonClient;
	private final OrderService orderService;

	public OrderCreateResponse purchaseOrder(final Long memberId,
		final OrderCreateRequest orderCreateRequest) {

		final Order order = OrderMapper.INSTANCE.orderCreateRequestToEntity(memberId, orderCreateRequest);

		final List<OrderItemDto> sortedDtoList = orderCreateRequest.orderItemDtoList().stream()
			.sorted(Comparator.comparing(OrderItemDto::itemId).thenComparing(OrderItemDto::itemOptionId))
			.toList();

		List<OrderItem> orderItemList = lockItemOption(sortedDtoList, order);
		OrderCreateResponse returnDto = orderService.saveOrder(orderItemList, order);

		return returnDto;
	}

	public List<OrderItem> lockItemOption(List<OrderItemDto> list, Order order) {
		List<OrderItem> returnList = new ArrayList<OrderItem>();
		RLock[] lockList = new RLock[list.size()];
		for (int i = 0; i < list.size(); i++) {
			lockList[i] = redissonClient.getLock(String.format("%d", list.get(i).itemOptionId()));
		}
		//RLock lockList = redissonClient.getLock(String.format("%d", list.get(0).itemOptionId()));
		RedissonMultiLock multiLock = new RedissonMultiLock(lockList);
		try {
			boolean available = multiLock.tryLock(100, 10, TimeUnit.SECONDS);
			//boolean available = lockList.tryLock(20, 6, TimeUnit.SECONDS);
			if (!available) {
				System.out.println("redisson getLock timeout");
				throw new IllegalArgumentException();
			} else {
				returnList = orderService.lockItemOptionList(list, order);
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			//lockList.unlock();
			multiLock.unlock();
		}
		return returnList;
	}

}

