package com.gabia.bshop.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gabia.bshop.dto.OrdersCreateRequestDto;
import com.gabia.bshop.dto.OrdersCreateResponseDto;
import com.gabia.bshop.dto.OrdersDto;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.entity.Options;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.Orders;
import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.gabia.bshop.entity.enumtype.MemberGrade;
import com.gabia.bshop.entity.enumtype.MemberRole;
import com.gabia.bshop.entity.enumtype.OrderStatus;
import com.gabia.bshop.mapper.OrdersMapper;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.OptionsRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrdersRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

/*
    OrderService 기능들에 대한 단위 테스트
    1. 생성
    2. 취소
    3. (optional) 수정
 */

@ExtendWith(MockitoExtension.class)
class OrdersServiceTest {

	@Mock
	private OrdersRepository ordersRepository;

	@Mock
	private OrderItemRepository orderItemRepository;

	@Mock
	private ItemImageRepository itemImageRepository;

	@Mock
	private MemberRepository memberRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OptionsRepository optionsRepository;

    @InjectMocks
    private OrdersService ordersService;

	@Test
	void 존재하지_않는_회원ID로_주문목록_조회를_요청하면_오류가_발생한다() {
		//given
		Long invalidMemberId = 999999999999L;
		when(memberRepository.findById(invalidMemberId))
			.thenThrow(EntityNotFoundException.class);
		//when & then
		Assertions.assertThatThrownBy(() -> ordersService.findOrdersPagination(invalidMemberId, PageRequest.of(0, 10)))
			.isInstanceOf(EntityNotFoundException.class);
	}

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
                        .deleted(false)
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
                        .deleted(false)
                        .openAt(LocalDateTime.now())
                        .build();

        Options options1 = Options.builder()
                .id(1L)
                .item(item1)
                .description("description")
                .optionLevel(1)
                .optionPrice(0)
                .stockQuantity(10)
                .build();

        Options options2 = Options.builder()
                .id(2L)
                .item(item2)
                .description("description")
                .optionLevel(1)
                .optionPrice(1000)
                .stockQuantity(5)
                .build();

        Orders orders = Orders.builder()
                .id(1L)
                .member(member)
                .status(OrderStatus.ACCEPTED)
                .build();

        OrderItem orderItem1 = OrderItem.builder()
                .id(1L)
                .item(item1)
                .order(orders)
                .orderCount(1)
                .price(item1.getBasePrice() + options1.getOptionPrice())
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .id(2L)
                .item(item2)
                .order(orders)
                .orderCount(1)
                .price(item2.getBasePrice() + options2.getOptionPrice())
                .build();

        List<OrderItem> orderItemList = List.of(orderItem1, orderItem2);

        List<OrdersDto> ordersDtoList = OrdersMapper.INSTANCE.orderItemListToOrderDtoList(
                orderItemList);

        OrdersCreateRequestDto ordersCreateRequestDto = OrdersCreateRequestDto.builder()
                .memberId(1L)
                .status(OrderStatus.ACCEPTED)
                .orderItems(ordersDtoList)
                .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(member));
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item1));
        when(itemRepository.findById(2L)).thenReturn(Optional.ofNullable(item2));
        when(optionsRepository.findByItem_Id(1L)).thenReturn(options1);
        when(optionsRepository.findByItem_Id(2L)).thenReturn(options2);

        //when
        OrdersCreateResponseDto returnDto = ordersService.createOrder(ordersCreateRequestDto);

        //then
        assertAll(
                () -> assertEquals(returnDto.orderItems(), ordersDtoList),
                () -> assertEquals(returnDto.totalPrice(),
                        orderItemList.stream().mapToLong(OrderItem::getPrice).sum()),
                () -> assertEquals(returnDto.memberId(), ordersCreateRequestDto.memberId()),
                () -> assertEquals(returnDto.status(), ordersCreateRequestDto.status())
        );
    }

    @DisplayName("주문을 취소한다.")
    @Test
    void cancelOrder() {
        //given
        Options options1 = Options.builder()
                .id(1L)
                .description("description")
                .optionLevel(1)
                .optionPrice(0)
                .stockQuantity(10)
                .build();

        List<Options> options = List.of(options1);

        Item item1 =
                Item.builder()
                        .id(1L)
                        .name("item")
                        .itemStatus(ItemStatus.PUBLIC)
                        .basePrice(10000)
                        .description("description")
                        .deleted(false)
                        .openAt(LocalDateTime.now())
                        .optionsList(options)
                        .build();

        OrderItem orderItem1 = OrderItem.builder()
                .id(1L)
                .item(item1)
                .orderCount(5)
                .build();

        List<OrderItem> orderItemList = List.of(orderItem1);

        Orders orders = Orders.builder()
                .id(1L)
                .status(OrderStatus.ACCEPTED)
                .totalPrice(20000)
                .orderItems(orderItemList)
                .build();

        when(ordersRepository.findById(1L)).thenReturn(Optional.ofNullable(orders));

        //when
        ordersService.cancelOrder(1L);

        //then
        assertAll(
                () -> assertEquals(15, options1.getStockQuantity(), "주문을 취소하면 재고가 다시 추가되어야 한다."),
                () -> assertEquals(OrderStatus.CANCELLED, orders.getStatus(),
                        "주문을 취소하면 주문상태가 CANCELLED로 변경되어야 한다.")
        );
    }

    @DisplayName("주문 ID가 유효하지 않으면 주문취소에 실패한다.")
    @Test
    void cancelOrderFail() {
        //given
        Long nonId = 9999L;

        when(ordersRepository.findById(nonId)).thenThrow(EntityNotFoundException.class);

        //when & then
        org.junit.jupiter.api.Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> ordersService.cancelOrder(nonId));
    }
}


