package com.gabia.bshop.service;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.dto.OrdersCreateRequestDto;
import com.gabia.bshop.dto.OrdersCreateResponseDto;
import com.gabia.bshop.entity.*;
import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.gabia.bshop.entity.enumtype.MemberGrade;
import com.gabia.bshop.entity.enumtype.MemberRole;
import com.gabia.bshop.entity.enumtype.OrderStatus;
import com.gabia.bshop.mapper.MemberMapper;
import com.gabia.bshop.mapper.OrderItemMapper;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrdersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/*
    OrderService 기능들에 대한 단위 테스트
    1. 생성
    2. 제거
    3. (optional) 수정
 */

//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class OrdersServiceTest {

    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrdersService ordersService;

    @Test
    void 주문_생성() {
        //given
        Member member = Member.builder()
                .id(1L)
                .email("test@test.com")
                .grade(MemberGrade.SILVER)
                .name("dummy")
                .phoneNumber("01076764563")
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

        Orders orders = Orders.builder()
                .id(1L)
                .member(member)
                .status(OrderStatus.ACCEPTED)
                .totalPrice(20000)
                .build();

        OrderItem orderItem1 = OrderItem.builder()
                .id(1L)
                .item(item1)
                .order(orders)
                .orderCount(1)
                .price(10000)
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .id(2L)
                .item(item1)
                .order(orders)
                .orderCount(1)
                .price(10000)
                .build();

        List<OrderItem> orderItemList = List.of(orderItem1,orderItem2);

        List<OrderItemDto> orderItemDtos = orderItemList.stream().map(
                OrderItemMapper.INSTANCE::orderItemDto).toList();

        //when
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(member));
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item1));
        when(itemRepository.findById(2L)).thenReturn(Optional.ofNullable(item2));

        when(orderItemRepository.save(orderItem1)).thenReturn(orderItem1);
        when(orderItemRepository.save(orderItem1)).thenReturn(orderItem2);


        OrdersCreateRequestDto ordersCreateRequestDto = OrdersCreateRequestDto.builder()
                .memberId(1L)
                .status(OrderStatus.ACCEPTED)
                .itemDtoList(orderItemDtos)
                .totalPrice(20000)
                .build();

        OrdersCreateResponseDto returnDto = ordersService.createOrders(ordersCreateRequestDto);

        //then
        assertAll(
                () -> assertEquals(returnDto.itemDtoList(), orderItemDtos),
                () -> assertEquals(returnDto.totalPrice(), ordersCreateRequestDto.totalPrice()),
                () -> assertEquals(returnDto.memberDto(), MemberMapper.INSTANCE.memberToDto(member)),
                () -> assertEquals(returnDto.status(), ordersCreateRequestDto.status())
        );

    }

    @Test
    void 주문_제거() {
        //given
        Orders orders = Orders.builder()
                .id(1L)
                .status(OrderStatus.ACCEPTED)
                .totalPrice(20000)
                .build();

        //when
        when(ordersRepository.findById(1L)).thenReturn(Optional.ofNullable(orders));

        ordersService.deleteOrders(1L);
        //then
        verify(ordersRepository, times(1)).delete(any());

    }

    @Test
    void 주문_제거_실패(){
        //given
        Long nonId = 9999L;

        //when
        when(ordersRepository.findById(nonId)).thenThrow(EntityNotFoundException.class);

        //when & then
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> {
                    ordersService.deleteOrders(nonId);
                });

    }
}