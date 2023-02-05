package com.gabia.bshop.service;

import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrdersRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/*
    OrderService 기능들에 대한 단위 테스트
    1. 생성
    2. 제거
    3. (optional) 수정
 */

@SpringBootTest
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

    }

    @Test
    void 주문_제거() {

    }
}