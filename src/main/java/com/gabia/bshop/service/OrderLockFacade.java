package com.gabia.bshop.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.dto.request.OrderCreateRequestDto;
import com.gabia.bshop.dto.response.OrderCreateResponseDto;
import com.gabia.bshop.entity.Order;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.repository.ItemOptionRepository;
import com.gabia.bshop.repository.OrderRepository;

@Service
public class OrderLockFacade {

	private final OrderService orderService;
	private final RedissonClient redissonClient;
	private final ItemOptionRepository itemOptionRepository;
	private final OrderRepository orderRepository;

	public OrderLockFacade(OrderService orderService, RedissonClient redissonClient,
		ItemOptionRepository itemOptionRepository, OrderRepository orderRepository) {
		this.orderService = orderService;
		this.redissonClient = redissonClient;
		this.itemOptionRepository = itemOptionRepository;
		this.orderRepository = orderRepository;
	}



	// public void cancelOrder(final Long memberId, final Long orderId) {
	// 	RLock lock = redissonClient.getLock(String.format("purchase:%d", memberId));
	// 	try {
	// 		boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
	// 		if (!available) {
	// 			System.out.println("redisson getLock timeout");
	// 			throw new IllegalArgumentException();
	// 		}
	// 		orderService.cancelOrder(memberId, orderId);
	// 	} catch (InterruptedException e) {
	// 		throw new RuntimeException(e);
	// 	} finally {
	// 		lock.unlock();
	// 	}
	// }

	public OrderCreateResponseDto purchase(final Long memberId, final OrderCreateRequestDto orderCreateRequestDto) {

		Order order = orderService.createOrder1(memberId, orderCreateRequestDto);
		final List<OrderItemDto> sortedDtoList = orderCreateRequestDto.orderItemDtoList().stream()
			.sorted(Comparator.comparing(OrderItemDto::itemId).thenComparing(OrderItemDto::itemOptionId))
			.toList();

		//lock
		List<OrderItem> orderItemList = lockItemOption(orderCreateRequestDto.orderItemDtoList(), order);
		//List<OrderItem> orderItemList = orderService.lockItemOptionList(sortedDtoList, order);
		//lock finish
		OrderCreateResponseDto res = orderService.saveOrder(order, orderItemList);

		return res;
	}

	public List<OrderItem> lockItemOption(List<OrderItemDto> list, Order order) {
		List<OrderItem> returnList = new ArrayList<>();
		List<RLock> lockList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			RLock lock = redissonClient.getLock(String.format("%d", list.get(i).itemOptionId()));
			lockList.add(lock);
		}
		try {
			boolean available = false;
			for (RLock r : lockList) {
				available = r.tryLock(20, 20, TimeUnit.SECONDS);
			}

			if (!available) {
				System.out.println("redisson getLock timeout");
				throw new IllegalArgumentException();
			} else {
				//System.out.println("lockname:" + lock.getName());
				returnList = orderService.lockItemOptionList(list, order);
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lockList.forEach(rLock -> rLock.unlock());
		}

		return returnList;
	}
}
