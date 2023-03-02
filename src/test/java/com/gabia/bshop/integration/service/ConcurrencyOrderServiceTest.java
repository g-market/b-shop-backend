package com.gabia.bshop.integration.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.dto.request.ItemOptionRequest;
import com.gabia.bshop.dto.request.OrderCreateRequest;
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
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.repository.ItemOptionRepository;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrderRepository;
import com.gabia.bshop.service.ItemOptionService;
import com.gabia.bshop.service.OrderService;

import jakarta.persistence.EntityManager;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ConcurrencyOrderServiceTest extends IntegrationTest {

	static int idx = 0;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ItemOptionRepository itemOptionRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private ItemImageRepository itemImageRepository;

	@Autowired
	private OrderService orderService;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ItemOptionService itemOptionService;

	@BeforeEach
	void setUp() {
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
			.name("카테고리" + idx++)
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

		int stockQuantity = 300;

		ItemOption itemOption1 = ItemOption.builder()
			.item(item1)
			.description("temp_itemOption1_description")
			.optionPrice(0)
			.stockQuantity(stockQuantity)
			.build();
		ItemOption itemOption2 = ItemOption.builder()
			.item(item2)
			.description("temp_itemOption2_description")
			.optionPrice(1000)
			.stockQuantity(stockQuantity)
			.build();
		ItemOption itemOption3 = ItemOption.builder()
			.item(item3)
			.description("temp_itemOption3_description")
			.optionPrice(1000)
			.stockQuantity(stockQuantity)
			.build();
		ItemOption itemOption4 = ItemOption.builder()
			.item(item4)
			.description("temp_itemOption4_description")
			.optionPrice(1000)
			.stockQuantity(stockQuantity)
			.build();
		ItemOption itemOption5 = ItemOption.builder()
			.item(item5)
			.description("temp_itemOption5_description")
			.optionPrice(1000)
			.stockQuantity(stockQuantity)
			.build();
		ItemOption itemOption6 = ItemOption.builder()
			.item(item6)
			.description("temp_itemOption6_description")
			.optionPrice(1000)
			.stockQuantity(stockQuantity)
			.build();
		ItemOption itemOption7 = ItemOption.builder()
			.item(item7)
			.description("temp_itemOption7_description")
			.optionPrice(1000)
			.stockQuantity(stockQuantity)
			.build();
		ItemOption itemOption8 = ItemOption.builder()
			.item(item8)
			.description("temp_itemOption8_description")
			.optionPrice(1000)
			.stockQuantity(stockQuantity)
			.build();
		ItemOption itemOption9 = ItemOption.builder()
			.item(item9)
			.description("temp_itemOption9_description")
			.optionPrice(1000)
			.stockQuantity(stockQuantity)
			.build();
		ItemOption itemOption10 = ItemOption.builder()
			.item(item9)
			.description("temp_itemOption10_description")
			.optionPrice(1000)
			.stockQuantity(stockQuantity)
			.build();

		memberRepository.saveAll(List.of(member1, member2, member3, member4, member5));
		categoryRepository.save(category1);
		itemRepository.saveAll(List.of(item1, item2, item3, item4, item5, item6, item7, item8, item9));
		itemOptionRepository.saveAll(
			List.of(itemOption1, itemOption2, itemOption3, itemOption4, itemOption5, itemOption6, itemOption7,
				itemOption8, itemOption9, itemOption10));
	}

	@AfterEach
	public void afterEach() {
		orderItemRepository.deleteAll();
		orderRepository.deleteAll();
		itemOptionRepository.deleteAll();
		itemRepository.deleteAll();
		categoryRepository.deleteAll();
		memberRepository.deleteAll();
	}

	@DisplayName("동시에_100명이_주문을_한다.")
	@Test
	void concurrencyOrder() throws InterruptedException {

		List<ItemOption> itemOptionList = itemOptionRepository.findAll();

		OrderItemDto orderItemDto1 = OrderItemDto.builder()
			.itemId(1L)
			.itemOptionId(1L)
			.orderCount(1)
			.build();
		OrderItemDto orderItemDto3 = OrderItemDto.builder()
			.itemId(3L)
			.itemOptionId(3L)
			.orderCount(1)
			.build();
		OrderItemDto orderItemDto10 = OrderItemDto.builder()
			.itemId(9L)
			.itemOptionId(10L)
			.orderCount(1)
			.build();

		List<OrderItemDto> orderItemDtoList = List.of(orderItemDto1, orderItemDto3, orderItemDto10);

		OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
			.orderItemDtoList(orderItemDtoList)
			.build();

		int nThreahdsSize = 1000;
		int repeatSize = 150;
		int countDownLatchSize = 300;
		ExecutorService executorService = Executors.newFixedThreadPool(nThreahdsSize);
		CountDownLatch countDownLatch = new CountDownLatch(countDownLatchSize);

		for (int i = 0; i < repeatSize; i++) {
			executorService.submit(() -> {
				try {
					orderService.createOrder(1L, orderCreateRequest);
				} catch (ConflictException e) {
					e.getMessage();
					//System.out.println("주문 실패");
				} finally {
					countDownLatch.countDown();
				}
			});
			executorService.submit(() -> {
				try {
					orderService.createOrder(2L, orderCreateRequest);
				} catch (ConflictException e) {
					e.getMessage();
					//System.out.println("주문 실패");
				} finally {
					countDownLatch.countDown();
				}
			});
		}
		countDownLatch.await();
		ItemOption AfterItemOption1 = itemOptionRepository.findById(1L).orElseThrow();
		ItemOption AfterItemOption3 = itemOptionRepository.findById(3L).orElseThrow();
		ItemOption AfterItemOption10 = itemOptionRepository.findById(10L).orElseThrow();

		List<Order> orderAll = orderRepository.findAll();
		List<OrderItem> oi1 = orderItemRepository.findAllByOptionId(1L);
		List<OrderItem> oi3 = orderItemRepository.findAllByOptionId(3L);
		List<OrderItem> oi10 = orderItemRepository.findAllByOptionId(10L);

		List<Order> oiO1 = orderRepository.findAllByMemberId(1L);
		List<Order> oiO2 = orderRepository.findAllByMemberId(2L);

		Assertions.assertThat(AfterItemOption1.getStockQuantity()).isEqualTo(0);
		Assertions.assertThat(AfterItemOption3.getStockQuantity()).isEqualTo(0);
		Assertions.assertThat(AfterItemOption10.getStockQuantity()).isEqualTo(0);
		Assertions.assertThat(oi1.size() * orderItemDto1.orderCount())
			.isEqualTo(itemOptionList.get(0).getStockQuantity());
		Assertions.assertThat(oi3.size() * orderItemDto3.orderCount())
			.isEqualTo(itemOptionList.get(3).getStockQuantity());
		Assertions.assertThat(oi10.size() * orderItemDto10.orderCount())
			.isEqualTo(itemOptionList.get(3).getStockQuantity());
		Assertions.assertThat(orderAll.size()).isEqualTo(itemOptionList.get(0).getStockQuantity());

	}

	@DisplayName("동시에_주문을_취소한다.")
	@Test
	void concurrencyOrderCancel() throws InterruptedException {

		List<ItemOption> itemOptionList = itemOptionRepository.findAll();

		List<OrderItemDto> orderItemDtoList = new ArrayList<>();
		for (ItemOption now : itemOptionList) {
			OrderItemDto orderItemDto = OrderItemDto.builder()
				.itemId(now.getItem().getId())
				.itemOptionId(now.getId())
				.orderCount(1)
				.build();

			orderItemDtoList.add(orderItemDto);
		}

		OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
			.orderItemDtoList(orderItemDtoList)
			.build();

		int nThreahdsSize = 1000;
		int repeatSize = 300;
		int countDownLatchSize = 300;
		List<Long> orderIds = orderRepository.findAll().stream().map(order -> order.getId()).toList();
		int repeatCount = orderIds.size();
		ExecutorService executorService = Executors.newFixedThreadPool(nThreahdsSize);
		CountDownLatch countDownLatch = new CountDownLatch(countDownLatchSize);

		//n개를 주문한다.
		for (int i = 0; i < repeatSize; i++) {
			executorService.submit(() -> {
				try {
					orderService.createOrder(1L, orderCreateRequest);
				} catch (ConflictException e) {
					e.getMessage();
					//System.out.println("주문 실패");
				} finally {
					countDownLatch.countDown();
				}
			});
		}
		countDownLatch.await();

		//n개를 동시에 주문 취소한다.
		CountDownLatch countDownLatch2 = new CountDownLatch(repeatCount);
		for (Long orderId : orderIds) {
			executorService.submit(() -> {
				try {
					orderService.cancelOrder(1L, orderId);
				} finally {
					countDownLatch.countDown();
				}
			});
		}
		countDownLatch2.await();

		ItemOption itemOption1 = itemOptionList.get(0);
		ItemOption AfterItemOption1 = itemOptionRepository.findByIdAndItemId(itemOption1.getId(),
			itemOption1.getItem().getId()).orElseThrow();

		Assertions.assertThat(itemOption1.getStockQuantity()).isEqualTo(AfterItemOption1.getStockQuantity());
	}

	@DisplayName("재고를_업데이트_한다.")
	@Test
	void concurrencyStockUpdate() throws InterruptedException {

		List<ItemOption> itemOptionList = itemOptionRepository.findAll();

		List<OrderItemDto> orderItemDtoList = new ArrayList<>();
		for (ItemOption now : itemOptionList) {
			OrderItemDto orderItemDto = OrderItemDto.builder()
				.itemId(now.getItem().getId())
				.itemOptionId(now.getId())
				.orderCount(1)
				.build();

			orderItemDtoList.add(orderItemDto);
		}

		OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
			.orderItemDtoList(orderItemDtoList)
			.build();

		ItemOptionRequest itemOptionRequest = ItemOptionRequest.builder()
			.description("item description")
			.stockQuantity(0)
			.build();

		int nThreahdsSize = 1000;
		int repeatSize = 150;
		int countDownLatchSize = 301;
		ExecutorService executorService = Executors.newFixedThreadPool(nThreahdsSize);
		CountDownLatch countDownLatch = new CountDownLatch(countDownLatchSize);

		ItemOption itemOption1 = itemOptionList.get(0);

		for (int i = 0; i < repeatSize; i++) {
			executorService.submit(() -> {
				try {
					orderService.createOrder(1L, orderCreateRequest);
				} catch (ConflictException e) {
					e.getMessage();
					//System.out.println("주문 실패");
				} finally {
					countDownLatch.countDown();
				}
			});
			executorService.submit(() -> {
				try {
					orderService.createOrder(2L, orderCreateRequest);
				} catch (ConflictException e) {
					e.getMessage();
					//System.out.println("주문 실패");
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		//재고를 업데이트한다.
		executorService.submit(() -> {
			try {
				itemOptionService.changeItemOption(itemOption1.getItem().getId(), itemOption1.getId(),
					itemOptionRequest);
			} catch (ConflictException e) {
				e.getMessage();
				//System.out.println("주문 실패");
			} finally {
				countDownLatch.countDown();
			}
		});

		countDownLatch.await();

		List<OrderItem> orderItemList1 = orderItemRepository.findAllByOptionId(itemOption1.getId());
		ItemOption AfterItemOption1 = itemOptionRepository.findByIdAndItemId(itemOption1.getId(),
			itemOption1.getItem().getId()).orElseThrow();
		List<Order> order1_member1 = orderRepository.findAllByMemberId(1L);
		List<Order> order2_member2 = orderRepository.findAllByMemberId(2L);

		Assertions.assertThat(AfterItemOption1.getStockQuantity()).isEqualTo(0);
		Assertions.assertThat(orderItemList1.size()).isLessThanOrEqualTo(itemOption1.getStockQuantity());
		Assertions.assertThat(order1_member1.size() + order2_member2.size()).isEqualTo(orderItemList1.size());
	}
}
