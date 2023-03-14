package com.gabia.bshop.integration.service;

import static com.gabia.bshop.fixture.MemberFixture.*;

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
import org.springframework.boot.test.context.SpringBootTest;

import com.gabia.bshop.config.ImageDefaultProperties;
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
import com.gabia.bshop.exception.ConflictException;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemOptionRepository;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrderRepository;
import com.gabia.bshop.service.ItemOptionService;
import com.gabia.bshop.service.OrderService;

@SpringBootTest
public class ConcurrencyOrderServiceTest {

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
	private OrderService orderService;

	@Autowired
	private ItemOptionService itemOptionService;

	@Autowired
	private ImageDefaultProperties imageDefaultProperties;

	@BeforeEach
	void setUp() {
		LocalDateTime now = LocalDateTime.now();
		Member member1 = JENNA.getInstance();
		Member member2 = JAIME.getInstance();

		Category category1 = Category.builder().name("카테고리" + idx++).build();

		Item item1 = Item.builder()
			.category(category1)
			.name("temp_item_name1")
			.description("temp_item_1_description " + UUID.randomUUID())
			.basePrice(11111)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item2 = Item.builder()
			.category(category1)
			.name("temp_item_name2")
			.description("temp_item_2_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item3 = Item.builder()
			.category(category1)
			.name("temp_item_name3")
			.description("temp_item_3_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item4 = Item.builder()
			.category(category1)
			.name("temp_item_name4")
			.description("temp_item_4_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item5 = Item.builder()
			.category(category1)
			.name("temp_item_name5")
			.description("temp_item_5_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item6 = Item.builder()
			.category(category1)
			.name("temp_item_name6")
			.description("temp_item_6_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item7 = Item.builder()
			.category(category1)
			.name("temp_item_name7")
			.description("temp_item_7_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item8 = Item.builder()
			.category(category1)
			.name("temp_item_name8")
			.description("temp_item_8_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
			.build();
		Item item9 = Item.builder()
			.category(category1)
			.name("temp_item_name9")
			.description("temp_item_9_description " + UUID.randomUUID())
			.basePrice(22222)
			.itemStatus(ItemStatus.PUBLIC)
			.openAt(now)
			.thumbnail(imageDefaultProperties.getItemImageUrl())
			.year(2023)
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
			.stockQuantity(200)
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
			.stockQuantity(500)
			.build();

		memberRepository.saveAll(List.of(member1, member2));
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
		memberRepository.deleteAll();
		itemOptionRepository.deleteAll();
		itemRepository.deleteAll();
		categoryRepository.deleteAll();
	}

	@DisplayName("동시에_1000명이_주문을_한다.")
	@Test
	void concurrencyOrder() throws InterruptedException {

		ItemOption beforeItemOption3 = itemOptionRepository.findByIdAndItemId(3L, 3L).orElseThrow();
		ItemOption beforeItemOption10 = itemOptionRepository.findByIdAndItemId(10L, 9L).orElseThrow();

		OrderItemDto orderItemDto1 = OrderItemDto.builder().itemId(1L).itemOptionId(1L).orderCount(1).build();
		OrderItemDto orderItemDto3 = OrderItemDto.builder().itemId(3L).itemOptionId(3L).orderCount(1).build();
		OrderItemDto orderItemDto9 = OrderItemDto.builder().itemId(9L).itemOptionId(10L).orderCount(1).build();

		OrderItemDto orderItemDto10 = OrderItemDto.builder().itemId(9L).itemOptionId(10L).orderCount(2).build();

		List<OrderItemDto> orderItemDtoList = List.of(orderItemDto1, orderItemDto3, orderItemDto9);
		List<OrderItemDto> orderItemDtoList2 = List.of(orderItemDto3, orderItemDto10);

		OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder().orderItemDtoList(orderItemDtoList).build();
		OrderCreateRequest orderCreateRequest2 = OrderCreateRequest.builder()
			.orderItemDtoList(orderItemDtoList2)
			.build();

		int nThreahdsSize = 1000;
		int repeatSize = 500;
		int countDownLatchSize = 1000;
		ExecutorService executorService = Executors.newFixedThreadPool(nThreahdsSize);
		CountDownLatch countDownLatch = new CountDownLatch(countDownLatchSize);

		for (int i = 0; i < repeatSize; i++) {
			executorService.submit(() -> {
				try {
					orderService.createOrder(1L, orderCreateRequest);
				} catch (ConflictException e) {
				} finally {
					countDownLatch.countDown();
				}
			});
			executorService.submit(() -> {
				try {
					orderService.createOrder(2L, orderCreateRequest2);
				} catch (ConflictException e) {
				} finally {
					countDownLatch.countDown();
				}
			});
		}
		countDownLatch.await();

		ItemOption afterItemOption3 = itemOptionRepository.findById(3L).orElseThrow();
		ItemOption afterItemOption10 = itemOptionRepository.findById(10L).orElseThrow();
		List<Order> orderAll = orderRepository.findAll();
		List<OrderItem> oi3 = orderItemRepository.findAllByOptionId(3L);
		List<OrderItem> oi10 = orderItemRepository.findAllByOptionId(10L);

		Assertions.assertThat(afterItemOption3.getStockQuantity()).isEqualTo(0); //3번 상품은 재고가 0이여야한다.

		Assertions.assertThat(oi3.stream().mapToInt(orderItem -> orderItem.getOrderCount()).sum())
			.isEqualTo(beforeItemOption3.getStockQuantity()); //3번 상품의 orderItem의 orderCount합과 기존 재고가 일치해야 한다.
		Assertions.assertThat(
				oi10.stream()
					.mapToInt(orderItem -> orderItem.getOrderCount()).sum() + afterItemOption10.getStockQuantity())
			.isEqualTo(beforeItemOption10.getStockQuantity());
		Assertions.assertThat(orderAll.size()).isEqualTo(beforeItemOption3.getStockQuantity());
	}

	@DisplayName("동시에_주문을_취소한다.")
	@Test
	void concurrencyOrderCancel() throws InterruptedException {

		List<ItemOption> beforeItemOptionList = itemOptionRepository.findAllByOrderByIdAsc();

		List<OrderItemDto> orderItemDtoList = new ArrayList<>();
		for (ItemOption now : beforeItemOptionList) {
			OrderItemDto orderItemDto = OrderItemDto.builder()
				.itemId(now.getItem().getId())
				.itemOptionId(now.getId())
				.orderCount(1)
				.build();
			orderItemDtoList.add(orderItemDto);
		}

		OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder().orderItemDtoList(orderItemDtoList).build();

		int nThreahdsSize = 1000;
		int repeatSize = 500;
		ExecutorService executorService = Executors.newFixedThreadPool(nThreahdsSize);
		CountDownLatch countDownLatch = new CountDownLatch(repeatSize);

		//n개를 주문한다.
		for (int i = 0; i < repeatSize; i++) {
			executorService.submit(() -> {
				try {
					orderService.createOrder(3L, orderCreateRequest);
				} catch (ConflictException e) {
				} finally {
					countDownLatch.countDown();
				}
			});
		}
		countDownLatch.await();

		List<Long> orderIds = orderRepository.findAll().stream().map(order -> order.getId()).toList();
		int repeatCount = orderIds.size();

		//n개를 동시에 주문 취소한다.
		CountDownLatch countDownLatch2 = new CountDownLatch(repeatCount);
		for (Long orderId : orderIds) {
			executorService.submit(() -> {
				try {
					orderService.cancelOrder(3L, orderId);
				} finally {
					countDownLatch2.countDown();
				}
			});
		}
		countDownLatch2.await();

		List<ItemOption> afterItemOptionList = itemOptionRepository.findAllByOrderByIdAsc();

		// 주문 취소 후 처음 재고와 일치하는지 확인
		for (int i = 0; i < afterItemOptionList.size(); i++) {
			Assertions.assertThat(afterItemOptionList.get(i).getStockQuantity())
				.isEqualTo(beforeItemOptionList.get(i).getStockQuantity());
		}
	}

	@DisplayName("주문과_동시에_재고를_업데이트_한다.")
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

		OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder().orderItemDtoList(orderItemDtoList).build();

		ItemOptionRequest itemOptionRequest = new ItemOptionRequest("item description", 0, 0);

		int nThreahdsSize = 1000;
		int repeatSize = 500;
		int countDownLatchSize = 1001;
		ExecutorService executorService = Executors.newFixedThreadPool(nThreahdsSize);
		CountDownLatch countDownLatch = new CountDownLatch(countDownLatchSize);

		ItemOption itemOption1 = itemOptionList.get(0);

		//n개를 주문한다.
		for (int i = 0; i < repeatSize; i++) {
			executorService.submit(() -> {
				try {
					orderService.createOrder(5L, orderCreateRequest);
				} catch (ConflictException e) {
				} finally {
					countDownLatch.countDown();
				}
			});
			executorService.submit(() -> {
				try {
					orderService.createOrder(6L, orderCreateRequest);
				} catch (ConflictException e) {
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		//재고를 0개로 업데이트한다.
		executorService.submit(() -> {
			try {
				itemOptionService.changeItemOption(itemOption1.getItem().getId(), itemOption1.getId(),
					itemOptionRequest);
			} catch (ConflictException e) {
			} finally {
				countDownLatch.countDown();
			}
		});

		countDownLatch.await();

		List<OrderItem> orderItemList1 = orderItemRepository.findAllByOptionId(itemOption1.getId());
		ItemOption AfterItemOption1 = itemOptionRepository.findByIdAndItemId(itemOption1.getId(),
			itemOption1.getItem().getId()).orElseThrow();
		List<Order> order1_member1 = orderRepository.findAllByMemberId(5L);
		List<Order> order2_member2 = orderRepository.findAllByMemberId(6L);

		Assertions.assertThat(AfterItemOption1.getStockQuantity()).isEqualTo(0);//재고가 0개로 update
		Assertions.assertThat(orderItemList1.size()).isLessThanOrEqualTo(itemOption1.getStockQuantity());
		Assertions.assertThat(order1_member1.size() + order2_member2.size()).isEqualTo(orderItemList1.size());
	}
}
