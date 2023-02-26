package com.gabia.bshop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.dto.request.OrderCreateRequestDto;
import com.gabia.bshop.dto.request.OrderUpdateStatusRequest;
import com.gabia.bshop.dto.response.OrderCreateResponseDto;
import com.gabia.bshop.dto.response.OrderInfoSingleResponse;
import com.gabia.bshop.dto.response.OrderUpdateStatusResponse;
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
import com.gabia.bshop.exception.BadRequestException;
import com.gabia.bshop.mapper.OrderMapper;
import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.repository.ItemOptionRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrderRepository;
import com.gabia.bshop.security.MemberPayload;

import jakarta.persistence.EntityNotFoundException;

/*
    OrderService 기능들에 대한 단위 테스트
    1. 생성
    2. 취소
    3. (optional) 수정
 */

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderItemRepository orderItemRepository;

	@Mock
	private ItemOptionRepository itemOptionRepository;

	@Mock
	private ItemImageRepository imageRepository;

	@InjectMocks
	private OrderService orderService;

	@DisplayName("주문을 생성한다.")
	@Test
	void createOrder() {
		//given
		Member member = Member.builder()
			.id(1L)
			.email("test@test.com")
			.grade(MemberGrade.BRONZE)
			.name("testName")
			.phoneNumber("01000001111")
			.hiworksId("hiworks")
			.role(MemberRole.NORMAL)
			.build();

		Category category = Category.builder().id(1L).name("name").build();

		Item item1 =
			Item.builder()
				.id(1L)
				.category(category)
				.name("item")
				.itemStatus(ItemStatus.PUBLIC)
				.basePrice(10000)
				.description("description")
				.openAt(LocalDateTime.now())
				.build();

		Item item2 =
			Item.builder()
				.id(2L)
				.category(category)
				.name("item")
				.itemStatus(ItemStatus.PUBLIC)
				.basePrice(10000)
				.description("description")
				.openAt(LocalDateTime.now())
				.build();

		ItemOption itemOption1 = ItemOption.builder()
			.id(1L)
			.item(item1)
			.description("description")
			.optionPrice(0)
			.stockQuantity(10)
			.build();

		ItemOption itemOption2 = ItemOption.builder()
			.id(2L)
			.item(item2)
			.description("description")
			.optionPrice(1000)
			.stockQuantity(5)
			.build();

		Order order = Order.builder()
			.id(1L)
			.member(member)
			.status(OrderStatus.ACCEPTED)
			.build();

		OrderItem orderItem1 = OrderItem.builder()
			.id(1L)
			.item(item1)
			.order(order)
			.option(itemOption1)
			.orderCount(1)
			.price(item1.getBasePrice() + itemOption1.getOptionPrice())
			.build();

		OrderItem orderItem2 = OrderItem.builder()
			.id(2L)
			.item(item2)
			.order(order)
			.option(itemOption2)
			.orderCount(1)
			.price(item2.getBasePrice() + itemOption2.getOptionPrice())
			.build();

		List<OrderItem> orderItemList = List.of(orderItem1, orderItem2);

		List<OrderItemDto> orderItemDtoList =
			OrderMapper.INSTANCE.orderItemListToOrderItemDtoList(orderItemList);

		OrderCreateRequestDto orderCreateRequestDto = OrderCreateRequestDto.builder()
			.status(OrderStatus.ACCEPTED)
			.orderItemDtoList(orderItemDtoList)
			.build();

		when(itemOptionRepository.findByItemIdListAndIdList(orderItemDtoList)).thenReturn(
			List.of(itemOption1, itemOption2));
		when(itemOptionRepository.findByItemIdListAndIdListWithLock(orderItemDtoList)).thenReturn(
			List.of(itemOption1, itemOption2));

		//when
		Order returnOrder = orderService.validateCreateOrder(member.getId(), orderCreateRequestDto);
		OrderCreateResponseDto returnDto = orderService.lockCreateOrder(orderItemDtoList, returnOrder);

		//then
		assertAll(
			() -> assertEquals(orderItemDtoList, returnDto.orderItemDtoList()),
			() -> assertEquals(orderItemList.stream().mapToLong(OrderItem::getPrice).sum(),
				returnDto.totalPrice()),
			() -> assertEquals(member.getId(), returnDto.memberId()),
			() -> assertEquals(orderCreateRequestDto.status(), returnDto.status()),
			() -> assertEquals(9, itemOption1.getStockQuantity(), "주문을 하면 재고가 줄어들어야 한다.")
		);
	}

	@DisplayName("유효하지_않은_아이템,옵션_ID가_들어오면_주문_생성에_실패한다.")
	@Test
	void createOrderFail() {
		//given
		Member member = Member.builder()
			.id(1L)
			.email("test@test.com")
			.grade(MemberGrade.BRONZE)
			.name("testName")
			.phoneNumber("01000001111")
			.hiworksId("hiworks")
			.role(MemberRole.NORMAL)
			.build();

		Category category = Category.builder().id(1L).name("name").build();

		Item item1 =
			Item.builder()
				.id(1L)
				.category(category)
				.name("item")
				.itemStatus(ItemStatus.PUBLIC)
				.basePrice(10000)
				.description("description")
				.openAt(LocalDateTime.now())
				.build();

		Item item2 =
			Item.builder()
				.id(2L)
				.category(category)
				.name("item")
				.itemStatus(ItemStatus.PUBLIC)
				.basePrice(10000)
				.description("description")
				.openAt(LocalDateTime.now())
				.build();

		ItemOption itemOption1 = ItemOption.builder()
			.id(1L)
			.item(item1)
			.description("description")
			.optionPrice(0)
			.stockQuantity(10)
			.build();

		ItemOption itemOption2 = ItemOption.builder()
			.id(2L)
			.item(item2)
			.description("description")
			.optionPrice(1000)
			.stockQuantity(5)
			.build();

		Order order = Order.builder()
			.id(1L)
			.member(member)
			.status(OrderStatus.ACCEPTED)
			.build();

		OrderItem orderItem1 = OrderItem.builder()
			.id(1L)
			.item(item1)
			.order(order)
			.option(itemOption1)
			.orderCount(1)
			.price(item1.getBasePrice() + itemOption1.getOptionPrice())
			.build();

		OrderItem orderItem2 = OrderItem.builder()
			.id(2L)
			.item(item1)
			.order(order)
			.option(itemOption2)
			.orderCount(1)
			.price(item2.getBasePrice() + itemOption2.getOptionPrice())
			.build();

		List<OrderItem> orderItemList = List.of(orderItem1, orderItem2);

		List<OrderItemDto> orderItemDtoList =
			OrderMapper.INSTANCE.orderItemListToOrderItemDtoList(orderItemList);

		OrderCreateRequestDto orderCreateRequestDto = OrderCreateRequestDto.builder()
			.status(OrderStatus.ACCEPTED)
			.orderItemDtoList(orderItemDtoList)
			.build();

		when(itemOptionRepository.findByItemIdListAndIdList(orderItemDtoList)).thenReturn(List.of(itemOption1));

		//when & then
		Assertions.assertThatThrownBy(() -> orderService.validateCreateOrder(member.getId(), orderCreateRequestDto))
			.isInstanceOf(BadRequestException.class);
	}

	@DisplayName("주문을_취소한다.")
	@Test
	void cancelOrder() {
		//given
		Member member = Member.builder()
			.id(1L)
			.email("test@test.com")
			.grade(MemberGrade.BRONZE)
			.name("testName")
			.phoneNumber("01000001111")
			.hiworksId("hiworks")
			.role(MemberRole.NORMAL)
			.build();

		ItemOption itemOption1 = ItemOption.builder()
			.id(1L)
			.description("description")
			.optionPrice(0)
			.stockQuantity(10)
			.build();

		List<ItemOption> options = List.of(itemOption1);

		Item item1 =
			Item.builder()
				.id(1L)
				.name("item")
				.itemStatus(ItemStatus.PUBLIC)
				.basePrice(10000)
				.description("description")
				.openAt(LocalDateTime.now())
				.build();
		item1.addItemOption(itemOption1);

		OrderItem orderItem1 = OrderItem.builder()
			.id(1L)
			.item(item1)
			.option(itemOption1)
			.orderCount(5)
			.build();

		List<OrderItem> orderItemList = List.of(orderItem1);

		Order order = Order.builder()
			.id(1L)
			.status(OrderStatus.ACCEPTED)
			.totalPrice(20000)
			.orderItemList(orderItemList)
			.build();

		when(orderRepository.findByIdAndMemberIdWithLock(order.getId(), member.getId())).thenReturn(
			Optional.ofNullable(order));

		//when
		orderService.cancelOrder(member.getId(), order.getId());

		//then
		assertAll(
			() -> assertEquals(15, itemOption1.getStockQuantity(), "주문을 취소하면 재고가 다시 추가되어야 한다."),
			() -> assertEquals(OrderStatus.CANCELLED, order.getStatus(),
				"주문을 취소하면 주문상태가 CANCELLED로 변경되어야 한다.")
		);
	}

	@DisplayName("주문_상태를_변경한다.")
	@Test
	void changeOrderStatus() {
		//given
		Order order = Order.builder()
			.id(1L)
			.status(OrderStatus.ACCEPTED)
			.totalPrice(20000)
			.build();

		when(orderRepository.findById(order.getId())).thenReturn(Optional.ofNullable(order));

		OrderUpdateStatusRequest reqDto = OrderUpdateStatusRequest.builder()
			.orderId(order.getId())
			.status(OrderStatus.COMPLETED)
			.build();

		//when
		OrderUpdateStatusResponse returnDto = orderService.updateOrderStatus(reqDto);

		//then
		assertEquals(reqDto.status(), returnDto.status(), "주문 상태 변경에 성공한다.");
	}

	@DisplayName("사용자는_본인의_주문이_아닐_경우_주문_단건_조회에_실패한다.")
	@Test
	void userFindSingleOrderFail() {
		//given
		Member member = Member.builder()
			.id(1L)
			.role(MemberRole.NORMAL)
			.build();

		Order order = Order.builder()
			.id(1L)
			.member(member)
			.status(OrderStatus.ACCEPTED)
			.totalPrice(20000)
			.build();

		MemberPayload memberPayload = MemberPayload.builder()
			.id(member.getId())
			.role(member.getRole())
			.build();

		when(orderRepository.findByIdAndMemberId(order.getId(), member.getId())).thenThrow(
			EntityNotFoundException.class);

		//when & then
		Assertions.assertThatThrownBy(() -> orderService.findSingleOrderInfo(memberPayload, order.getId()))
			.isInstanceOf(EntityNotFoundException.class);
	}

	@DisplayName("사용자는_본인의_주문일_경우_주문_단건_조회에_성공한다.")
	@Test
	void userFindSingleOrderSuccess() {
		//given
		Member member = Member.builder()
			.id(1L)
			.role(MemberRole.NORMAL)
			.build();

		Order order = Order.builder()
			.id(1L)
			.member(member)
			.status(OrderStatus.ACCEPTED)
			.totalPrice(20000)
			.build();

		ItemOption itemOption1 = ItemOption.builder()
			.id(1L)
			.description("description")
			.optionPrice(0)
			.stockQuantity(10)
			.build();

		List<ItemOption> options = List.of(itemOption1);

		Item item1 =
			Item.builder()
				.id(1L)
				.name("item")
				.itemStatus(ItemStatus.PUBLIC)
				.basePrice(10000)
				.description("description")
				.openAt(LocalDateTime.now())
				.build();

		OrderItem orderItem1 = OrderItem.builder()
			.id(1L)
			.order(order)
			.item(item1)
			.option(itemOption1)
			.orderCount(5)
			.build();

		List<OrderItem> orderItemList = List.of(orderItem1);

		String thumbnailUrl = "thumbnailUrl_test";
		List<String> thumbnailUrlList = List.of(thumbnailUrl);

		when(orderRepository.findByIdAndMemberId(order.getId(), member.getId())).thenReturn(Optional.ofNullable(order));
		when(orderItemRepository.findWithOrdersAndItemByOrderId(order.getId())).thenReturn(orderItemList);
		when(imageRepository.findUrlByItemIds(orderItemList.stream()
			.map(oi -> oi.getItem().getId())
			.collect(Collectors.toList()))).thenReturn(thumbnailUrlList);

		MemberPayload memberPayload = MemberPayload.builder()
			.id(member.getId())
			.role(member.getRole())
			.build();

		//when
		OrderInfoSingleResponse returnDto = orderService.findSingleOrderInfo(memberPayload, order.getId());

		//then
		assertEquals(order.getId(), returnDto.orderId(), "사용자는 본인의 주문일 경우 주문 단건 조회에 성공한다.");
	}

	@DisplayName("관리자가_주문_단건_조회를_한다.")
	@Test
	void adminFindSingleOrder() {
		//given
		Member member = Member.builder()
			.role(MemberRole.ADMIN)
			.build();

		Order order = Order.builder()
			.id(1L)
			.status(OrderStatus.ACCEPTED)
			.totalPrice(20000)
			.build();

		ItemOption itemOption1 = ItemOption.builder()
			.id(1L)
			.description("description")
			.optionPrice(0)
			.stockQuantity(10)
			.build();

		List<ItemOption> options = List.of(itemOption1);

		Item item1 =
			Item.builder()
				.id(1L)
				.name("item")
				.itemStatus(ItemStatus.PUBLIC)
				.basePrice(10000)
				.description("description")
				.openAt(LocalDateTime.now())
				.build();

		OrderItem orderItem1 = OrderItem.builder()
			.id(1L)
			.order(order)
			.item(item1)
			.option(itemOption1)
			.orderCount(5)
			.build();

		List<OrderItem> orderItemList = List.of(orderItem1);

		String thumbnailUrl = "thumbnailUrl_test";
		List<String> thumbnailUrlList = List.of(thumbnailUrl);

		when(orderRepository.findById(order.getId())).thenReturn(Optional.ofNullable(order));
		when(orderItemRepository.findWithOrdersAndItemByOrderId(order.getId())).thenReturn(orderItemList);
		when(imageRepository.findUrlByItemIds(orderItemList.stream()
			.map(oi -> oi.getItem().getId())
			.collect(Collectors.toList()))).thenReturn(thumbnailUrlList);

		MemberPayload memberPayload = MemberPayload.builder()
			.id(member.getId())
			.role(member.getRole())
			.build();

		//when
		OrderInfoSingleResponse returnDto = orderService.findSingleOrderInfo(memberPayload, order.getId());

		//then
		assertEquals(order.getId(), returnDto.orderId(), "관리자는 자신의 주문이 아니여도 단건 주문 조회에 성공한다.");
	}
}
