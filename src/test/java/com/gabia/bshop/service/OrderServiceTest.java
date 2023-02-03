package com.gabia.bshop.service;

import static org.junit.jupiter.api.Assertions.*;

import com.gabia.bshop.dto.response.OrderInfoPageResponse;
import com.gabia.bshop.entity.Category;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.Orders;
import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.gabia.bshop.entity.enumtype.MemberGrade;
import com.gabia.bshop.entity.enumtype.MemberRole;
import com.gabia.bshop.entity.enumtype.OrderStatus;
import com.gabia.bshop.integration.IntegrationTest;
import com.gabia.bshop.repository.CategoryRepository;
import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;


@Transactional
class OrderServiceTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ItemImageRepository itemImageRepository;

    @Autowired
    private OrderService orderService;

    @Test
    void 주문을_한_회원이_주문목록_조회를_수행하면_주문내역들이_조회되어야한다() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Member member1 = Member.builder()
                .name("1_test_name")
                .email("1_ckdals1234@naver.com")
                .hiworksId("1_asdfasdf")
                .phoneNumber("01000000001")
                .role(MemberRole.NORMAL)
                .grade(MemberGrade.BRONZE)
                .build();
        Category category1 = Category.builder()
                .name("카테고리1")
                .build();
        Item item1 = Item.builder()
                .category(category1)
                .name("temp_item_name1")
                .description("temp_item_1_description " + UUID.randomUUID().toString())
                .basePrice(11111)
                .itemStatus(ItemStatus.PUBLIC)
                .openAt(now)
                .deleted(false)
                .build();
        Item item2 = Item.builder()
                .category(category1)
                .name("temp_item_name1")
                .description("temp_item_1_description " + UUID.randomUUID().toString())
                .basePrice(22222)
                .itemStatus(ItemStatus.PUBLIC)
                .openAt(now)
                .deleted(false)
                .build();
        Orders order1 = Orders.builder()
                .member(member1)
                .status(OrderStatus.PENDING)
                .totalPrice(11111L)
                .build();
        OrderItem orderItem1_order1 = OrderItem.builder()
                .item(item1)
                .order(order1)
                .orderCount(1)
                .price(11111L)
                .build();
        Orders order2 = Orders.builder()
                .member(member1)
                .status(OrderStatus.PENDING)
                .totalPrice(33333L)
                .build();
        OrderItem orderItem2_order2 = OrderItem.builder()
                .item(item1)
                .order(order2)
                .orderCount(1)
                .price(11111L)
                .build();
        OrderItem orderItem3_order2 = OrderItem.builder()
                .item(item2)
                .order(order2)
                .orderCount(1)
                .price(22222L)
                .build();
        ItemImage itemImage1 = ItemImage.builder()
                .item(item1)
                .url(UUID.randomUUID().toString())
                .build();
        ItemImage itemImage2 = ItemImage.builder()
                .item(item1)
                .url(UUID.randomUUID().toString())
                .build();
        ItemImage itemImage3 = ItemImage.builder()
                .item(item2)
                .url(UUID.randomUUID().toString())
                .build();
        ItemImage itemImage4 = ItemImage.builder()
                .item(item2)
                .url(UUID.randomUUID().toString())
                .build();

        memberRepository.save(member1);
        categoryRepository.save(category1);
        itemRepository.saveAll(List.of(item1, item2));
        itemImageRepository.saveAll(List.of(itemImage1, itemImage2, itemImage3, itemImage4));
        orderRepository.saveAll(List.of(order1, order2));
        orderItemRepository.saveAll(List.of(orderItem1_order1, orderItem2_order2, orderItem3_order2));

        PageRequest pageable = PageRequest.of(0, 10);
        //when
        OrderInfoPageResponse orderInfo = orderService.findOrdersPagination(member1.getId(),
                pageable);
        //then
        Assertions.assertThat(orderInfo.resultCount()).isEqualTo(2);
        Assertions.assertThat(orderInfo.orderInfos().get(0).orderId()).isEqualTo(order1.getId());
        Assertions.assertThat(orderInfo.orderInfos().get(0).thumbnailImage()).isEqualTo(itemImage1.getUrl());
        Assertions.assertThat(orderInfo.orderInfos().get(0).representativeName()).isEqualTo(item1.getName());
        Assertions.assertThat(orderInfo.orderInfos().get(0).itemTotalCount()).isEqualTo(1);
        Assertions.assertThat(orderInfo.orderInfos().get(0).orderStatus()).isEqualTo(order1.getStatus());

        Assertions.assertThat(orderInfo.orderInfos().get(1).orderId()).isEqualTo(order2.getId());
        Assertions.assertThat(orderInfo.orderInfos().get(1).thumbnailImage()).isEqualTo(itemImage3.getUrl());
        Assertions.assertThat(orderInfo.orderInfos().get(1).representativeName()).isEqualTo(item2.getName());
        Assertions.assertThat(orderInfo.orderInfos().get(1).itemTotalCount()).isEqualTo(2);
        Assertions.assertThat(orderInfo.orderInfos().get(1).orderStatus()).isEqualTo(order2.getStatus());
    }
}
