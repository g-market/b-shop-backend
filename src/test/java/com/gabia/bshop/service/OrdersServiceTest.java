package com.gabia.bshop.service;

import static org.mockito.Mockito.when;


import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrdersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

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
}
