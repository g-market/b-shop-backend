package com.gabia.bshop.service;

import com.gabia.bshop.dto.OrdersCreateRequestDto;
import com.gabia.bshop.dto.OrdersCreateResponseDto;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.entity.Options;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.Orders;
import com.gabia.bshop.mapper.OrdersMapper;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.OptionsRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrdersRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OptionsRepository optionsRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * 주문 생성
     */
    public OrdersCreateResponseDto createOrder(OrdersCreateRequestDto ordersCreateRequestDto) {

        Member member = memberRepository.findById(ordersCreateRequestDto.memberId())
                .orElseThrow(EntityNotFoundException::new);
        Orders orders = OrdersMapper.INSTANCE.ordersCreateDtoToEntity(ordersCreateRequestDto);

        List<OrderItem> orderItemEntityList = ordersCreateRequestDto.itemList()
                .stream()
                .map(ol -> {
                    Item itemEntity = itemRepository.findById(ol.id()).get();
                    Options options = optionsRepository.findByItem_Id(ol.id());
                    return OrderItem.createOrderItem(itemEntity, options, orders, ol.orderCount());
                })
                .collect(Collectors.toList());

        ordersRepository.save(orders);
        orderItemRepository.saveAll(orderItemEntityList);

        return OrdersMapper.INSTANCE.orderCreateEntityToOrdersCreateResponseDto(orders,
                orderItemEntityList);
    }

    /**
     * 주문 제거
     */
    public void deleteOrder(Long id) {
        Orders orders = ordersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문 ID 입니다."));

        List<OrderItem> orderItemList = orderItemRepository.findAllByOrderId(id);

        orderItemRepository.deleteAll(orderItemList);
        ordersRepository.delete(orders);
    }
}
