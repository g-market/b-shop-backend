package com.gabia.bshop.integration.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.dto.request.OrderCreateRequestDto;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemOption;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.entity.Order;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.gabia.bshop.entity.enumtype.MemberGrade;
import com.gabia.bshop.entity.enumtype.MemberRole;
import com.gabia.bshop.entity.enumtype.OrderStatus;
import com.gabia.bshop.exception.ConflictException;
import com.gabia.bshop.integration.IntegrationTest;
import com.gabia.bshop.mapper.OrderMapper;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.repository.ItemOptionRepository;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrderRepository;
import com.gabia.bshop.service.OrderLockFacade;
import com.gabia.bshop.service.OrderService;

import jakarta.persistence.EntityManager;

public class OrderLockFacadeTest extends IntegrationTest {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ItemOptionRepository itemOptionRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private ItemImageRepository itemImageRepository;

	@Autowired
	private OrderService orderService;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private OrderLockFacade orderLockFacade;

	@DisplayName("동시에_100명이_주문을_한다.")
	@Test
	void concurrencyOrder() throws InterruptedException {
		LocalDateTime now = LocalDateTime.now();
		Member member1 = Member.builder()
			.name("1_test_name")
			.email("1_ckdals1234@naver.com")
			.hiworksId("1_asdfasdf")
			.phoneNumber("01000000001")
			.role(MemberRole.NORMAL)
			.grade(MemberGrade.BRONZE)
			.build();
		Member member2 = Member.builder()
			.name("2_test_name")
			.email("2_ckdals1234@naver.com")
			.hiworksId("2_asdfasdf")
			.phoneNumber("01022223333")
			.role(MemberRole.NORMAL)
			.grade(MemberGrade.BRONZE)
			.build();
		Member member3 = Member.builder()
			.name("3_test_name")
			.email("3_ckdals1234@naver.com")
			.hiworksId("3_asdfasdf")
			.phoneNumber("00011113333")
			.role(MemberRole.NORMAL)
			.grade(MemberGrade.BRONZE)
			.build();
		Member member4 = Member.builder()
			.name("4_test_name")
			.email("4_ckdals1234@naver.com")
			.hiworksId("4_asdfasdf")
			.phoneNumber("01022224444")
			.role(MemberRole.NORMAL)
			.grade(MemberGrade.BRONZE)
			.build();
		Member member5 = Member.builder()
			.name("5_test_name")
			.email("5_ckdals1234@naver.com")
			.hiworksId("5_asdfasdf")
			.phoneNumber("01055555555")
			.role(MemberRole.NORMAL)
			.grade(MemberGrade.BRONZE)
			.build();

		Category category1 = Category.builder()
			.name("카테고리1")
			.build();

		Item item1 = Item.builder()
			.category(category1)
			.name("temp_item_name1")
			.description("temp_item_1_description " + UUID.randomUUID())
			.basePrice(11111)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.build();
		Item item2 = Item.builder()
			.category(category1)
			.name("temp_item_name2")
			.description("temp_item_2_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.build();
		Item item3 = Item.builder()
			.category(category1)
			.name("temp_item_name3")
			.description("temp_item_3_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.build();
		Item item4 = Item.builder()
			.category(category1)
			.name("temp_item_name4")
			.description("temp_item_4_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.build();
		Item item5 = Item.builder()
			.category(category1)
			.name("temp_item_name5")
			.description("temp_item_5_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.build();
		Item item6 = Item.builder()
			.category(category1)
			.name("temp_item_name6")
			.description("temp_item_6_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.build();
		Item item7 = Item.builder()
			.category(category1)
			.name("temp_item_name7")
			.description("temp_item_7_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.build();
		Item item8 = Item.builder()
			.category(category1)
			.name("temp_item_name8")
			.description("temp_item_8_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.build();
		Item item9 = Item.builder()
			.category(category1)
			.name("temp_item_name9")
			.description("temp_item_9_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.build();


		ItemOption itemOption1 = ItemOption.builder()
			.item(item1)
			.description("temp_itemOption1_description")
			.optionPrice(0)
			.stockQuantity(300)
			.build();
		ItemOption itemOption2 = ItemOption.builder()
			.item(item2)
			.description("temp_itemOption2_description")
			.optionPrice(1000)
			.stockQuantity(300)
			.build();
		ItemOption itemOption3 = ItemOption.builder()
			.item(item3)
			.description("temp_itemOption3_description")
			.optionPrice(1000)
			.stockQuantity(300)
			.build();
		ItemOption itemOption4 = ItemOption.builder()
			.item(item4)
			.description("temp_itemOption4_description")
			.optionPrice(1000)
			.stockQuantity(300)
			.build();
		ItemOption itemOption5 = ItemOption.builder()
			.item(item5)
			.description("temp_itemOption5_description")
			.optionPrice(1000)
			.stockQuantity(300)
			.build();
		ItemOption itemOption6 = ItemOption.builder()
			.item(item6)
			.description("temp_itemOption6_description")
			.optionPrice(1000)
			.stockQuantity(300)
			.build();
		ItemOption itemOption7 = ItemOption.builder()
			.item(item7)
			.description("temp_itemOption7_description")
			.optionPrice(1000)
			.stockQuantity(300)
			.build();
		ItemOption itemOption8 = ItemOption.builder()
			.item(item8)
			.description("temp_itemOption8_description")
			.optionPrice(1000)
			.stockQuantity(300)
			.build();
		ItemOption itemOption9 = ItemOption.builder()
			.item(item9)
			.description("temp_itemOption9_description")
			.optionPrice(1000)
			.stockQuantity(300)
			.build();
		ItemOption itemOption10 = ItemOption.builder()
			.item(item9)
			.description("temp_itemOption10_description")
			.optionPrice(1000)
			.stockQuantity(300)
			.build();

		OrderItem orderItem1 = OrderItem.builder()
			.item(item1)
			.option(itemOption1)
			.orderCount(1)
			.build();
		OrderItem orderItem2 = OrderItem.builder()
			.item(item2)
			.option(itemOption2)
			.orderCount(1)
			.build();
		OrderItem orderItem3 = OrderItem.builder()
			.item(item3)
			.option(itemOption3)
			.orderCount(1)
			.build();
		OrderItem orderItem4 = OrderItem.builder()
			.item(item4)
			.option(itemOption4)
			.orderCount(1)
			.build();
		OrderItem orderItem5 = OrderItem.builder()
			.item(item5)
			.option(itemOption5)
			.orderCount(1)
			.build();
		OrderItem orderItem6 = OrderItem.builder()
			.item(item6)
			.option(itemOption6)
			.orderCount(1)
			.build();
		OrderItem orderItem7 = OrderItem.builder()
			.item(item7)
			.option(itemOption7)
			.orderCount(1)
			.build();
		OrderItem orderItem8 = OrderItem.builder()
			.item(item8)
			.option(itemOption8)
			.orderCount(1)
			.build();
		OrderItem orderItem9 = OrderItem.builder()
			.item(item9)
			.option(itemOption9)
			.orderCount(1)
			.build();
		OrderItem orderItem10 = OrderItem.builder()
			.item(item9)
			.option(itemOption10)
			.orderCount(1)
			.build();

		memberRepository.saveAll(List.of(member1, member2, member3, member4, member5));
		categoryRepository.save(category1);
		itemRepository.saveAll(List.of(item1, item2, item3, item4, item5, item6,item7,item8,item9));
		itemOptionRepository.saveAll(List.of(itemOption1, itemOption2, itemOption3,itemOption4,itemOption5,itemOption6,itemOption7,itemOption8,itemOption9,itemOption10));

		List<OrderItemDto> orderItemDtoList =
			OrderMapper.INSTANCE.orderItemListToOrderItemDtoList(List.of(orderItem1,orderItem3,orderItem10));

		List<OrderItemDto> orderItemDtoList2 =
			OrderMapper.INSTANCE.orderItemListToOrderItemDtoList(List
				.of(orderItem1,orderItem2,orderItem3,orderItem4,orderItem5,orderItem6,orderItem7,orderItem8,orderItem9,orderItem10));
		// List<OrderItemDto> orderItemDtoList3 =
		// 	OrderMapper.INSTANCE.orderItemListToOrderItemDtoList(List.of(orderItem4_order3, orderItem5_order3));
		//
		// List<OrderItemDto> orderItemDtoList4 =
		// 	OrderMapper.INSTANCE.orderItemListToOrderItemDtoList(List.of(orderItem6_order4));
		// List<OrderItemDto> orderItemDtoList5 =
		// 	OrderMapper.INSTANCE.orderItemListToOrderItemDtoList(List.of(orderItem7_order5));

		OrderCreateRequestDto orderCreateRequestDto = OrderCreateRequestDto.builder()
			.status(OrderStatus.ACCEPTED)
			.orderItemDtoList(orderItemDtoList)
			.build();
		OrderCreateRequestDto orderCreateRequestDto2 = OrderCreateRequestDto.builder()
			.status(OrderStatus.ACCEPTED)
			.orderItemDtoList(orderItemDtoList2)
			.build();
		// OrderCreateRequestDto orderCreateRequestDto3 = OrderCreateRequestDto.builder()
		// 	.status(OrderStatus.ACCEPTED)
		// 	.orderItemDtoList(orderItemDtoList3)
		// 	.build();
		// OrderCreateRequestDto orderCreateRequestDto4 = OrderCreateRequestDto.builder()
		// 	.status(OrderStatus.ACCEPTED)
		// 	.orderItemDtoList(orderItemDtoList4)
		// 	.build();
		// OrderCreateRequestDto orderCreateRequestDto5 = OrderCreateRequestDto.builder()
		// 	.status(OrderStatus.ACCEPTED)
		// 	.orderItemDtoList(orderItemDtoList5)
		// 	.build();

		int nThreahdsSize = 1000;
		int repeatSize =300;
		ExecutorService executorService = Executors.newFixedThreadPool(nThreahdsSize);
		CountDownLatch countDownLatch = new CountDownLatch(repeatSize);

		for (int i = 0; i < repeatSize; i++) {
			executorService.submit(() -> {
				try {
					orderLockFacade.purchase(member1.getId(), orderCreateRequestDto);//(1) (3) (10)
				} catch (ConflictException e) {
					e.getMessage();
					//System.out.println("주문 실패");
				} finally {
					countDownLatch.countDown();
				}
			});
			executorService.submit(() -> {
				try {
					orderLockFacade.purchase(member2.getId(), orderCreateRequestDto2);//(1) ~(10)
				} catch (ConflictException e) {
					e.getMessage();
					//System.out.println("주문 실패");
				} finally {
					countDownLatch.countDown();
				}
			});
		// 	executorService.submit(() -> {
		// 		try {
		// 			orderLockFacade.purchase(member3.getId(), orderCreateRequestDto3);//(2) (3)
		// 		} catch (ConflictException e) {
		// 			e.getMessage();
		// 			//System.out.println("주문 실패");
		// 		} finally {
		// 			countDownLatch.countDown();
		// 		}
		// 	});
		// 	executorService.submit(() -> {
		// 		try {
		// 			//orderLockFacade.purchase(member4.getId(), orderCreateRequestDto4);//(3)
		// 		} catch (ConflictException e) {
		// 			e.getMessage();
		// 			//System.out.println("주문 실패");
		// 		} finally {
		// 			countDownLatch.countDown();
		// 		}
		// 	});
		// 	executorService.submit(() -> {
		// 		try {
		// 			//orderLockFacade.purchase(member5.getId(), orderCreateRequestDto5);//(2)
		// 		} catch (ConflictException e) {
		// 			e.getMessage();
		// 			//System.out.println("주문 실패");
		// 		} finally {
		// 			countDownLatch.countDown();
		// 		}
		// 	});
		 }

		countDownLatch.await();
		ItemOption actual = itemOptionRepository.findById(itemOption1.getId()).orElseThrow();
		ItemOption actual2 = itemOptionRepository.findById(itemOption2.getId()).orElseThrow();
		ItemOption actual3 = itemOptionRepository.findById(itemOption3.getId()).orElseThrow();

		List<Order> orderAll = orderRepository.findAll();
		List<OrderItem> oi1 = orderItemRepository.findAllByOptionId(itemOption1.getId());
		List<OrderItem> oi2 = orderItemRepository.findAllByOptionId(itemOption2.getId());
		List<OrderItem> oi3 = orderItemRepository.findAllByOptionId(itemOption3.getId());

		List<Order> oiO1 = orderRepository.findAllByMemberId(member1.getId());
		List<Order> oiO2 = orderRepository.findAllByMemberId(member2.getId());
		List<Order> oiO3 = orderRepository.findAllByMemberId(member3.getId());
		List<Order> oiO4 = orderRepository.findAllByMemberId(member4.getId());
		List<Order> oiO5 = orderRepository.findAllByMemberId(member5.getId());

		System.out.println("1재고:" + actual.getStockQuantity());
		System.out.println("2재고:" + actual2.getStockQuantity());
		System.out.println("3재고:" + actual3.getStockQuantity());
		System.out.println(orderAll.size());
		System.out.println("1OI:" + oi1.size());
		System.out.println("2OI:" + oi2.size());
		System.out.println("3OI:" + oi3.size());

		System.out.println("member1:" + oiO1.size());
		System.out.println("member2:" + oiO2.size());
		System.out.println("member3:" + oiO3.size());
		System.out.println("member4:" + oiO4.size());
		System.out.println("member5:" + oiO5.size());

		// Assertions.assertThat(actual.getStockQuantity()).isEqualTo(0);
		// Assertions.assertThat(actual2.getStockQuantity()).isEqualTo(0);

		//executorService.shutdown();

	}

	@DisplayName("동시에_주문과_주문취소를_수행한다.")
	@Test
	void concurrencyPurchaseAndCancel() throws InterruptedException {
		LocalDateTime now = LocalDateTime.now();
		Member member1 = Member.builder()
			.name("2_test_name")
			.email("1_ckdals1234@naver.com")
			.hiworksId("1_asdfasdf")
			.phoneNumber("01000000001")
			.role(MemberRole.NORMAL)
			.grade(MemberGrade.BRONZE)
			.build();
		Category category1 = Category.builder()
			.name("카테고리1")
			.build();
		Item item2 = Item.builder()
			.category(category1)
			.name("temp_item_name1")
			.description("temp_item_1_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.build();
		ItemOption itemOption2 = ItemOption.builder()
			.item(item2)
			.description("temp_itemOption2_description")
			.optionPrice(1000)
			.stockQuantity(100)
			.build();
		Order order2 = Order.builder()
			.member(member1)
			.status(OrderStatus.ACCEPTED)
			.totalPrice(33333L)
			.build();
		OrderItem orderItem3_order2 = OrderItem.builder()
			.item(item2)
			.order(order2)
			.option(itemOption2)
			.orderCount(1)
			.price(22222L)
			.build();

		List<OrderItem> orderItemList = List.of(orderItem3_order2);

		OrderItemDto orderItemDto = OrderItemDto.builder()
			.itemId(1L)
			.itemOptionId(1L)
			.orderCount(1)
			.build();

		OrderItemDto orderItemDto2 = OrderItemDto.builder()
			.itemId(1L)
			.itemOptionId(1L)
			.orderCount(2)
			.build();

		List<OrderItemDto> orderItemDtoList = List.of(orderItemDto);
		List<OrderItemDto> orderItemDtoList2 = List.of(orderItemDto2);

		OrderCreateRequestDto orderCreateRequestDto = OrderCreateRequestDto.builder()
			.status(OrderStatus.ACCEPTED)
			.orderItemDtoList(orderItemDtoList)
			.build();
		OrderCreateRequestDto orderCreateRequestDto2 = OrderCreateRequestDto.builder()
			.status(OrderStatus.ACCEPTED)
			.orderItemDtoList(orderItemDtoList2)
			.build();

		int nThreahdsSize = 400;
		List<Long> orderIds = orderRepository.findAll().stream().map(order1 -> order1.getId()).toList();
		int repeatCount = orderIds.size();
		ExecutorService executorService = Executors.newFixedThreadPool(nThreahdsSize);
		CountDownLatch countDownLatch = new CountDownLatch(repeatCount);

		for (Long orderId : orderIds) {
			executorService.submit(() -> {
				try {
					//orderLockFacade.cancelOrder(1L, orderId);

					orderLockFacade.purchase(2L, orderCreateRequestDto2);
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();
		ItemOption actual = itemOptionRepository.findById(1L).orElseThrow();

		Assertions.assertThat(actual.getStockQuantity()).isZero();
	}
}
