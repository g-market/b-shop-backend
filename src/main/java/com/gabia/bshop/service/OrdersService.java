package com.gabia.bshop.service;

import com.gabia.bshop.dto.ItemDto;
import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.dto.OrdersCreateRequestDto;
import com.gabia.bshop.dto.OrdersCreateResponseDto;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.Orders;
import com.gabia.bshop.entity.enumtype.OrderStatus;
import com.gabia.bshop.mapper.ItemMapper;
import com.gabia.bshop.mapper.MemberMapper;
import com.gabia.bshop.mapper.OrderItemMapper;
import com.gabia.bshop.mapper.OrdersMapper;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrdersRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
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
    private final OrderItemRepository orderItemRepository;

    /**
     * 주문 생성
     */
    public OrdersCreateResponseDto createOrders(OrdersCreateRequestDto ordersCreateRequestDto){
        //Member 검색
        Member member = memberRepository.findById(ordersCreateRequestDto.memberId()).orElseThrow(EntityNotFoundException::new);

        //Item 검색
//        List<Item> itemList = itemRepository.findAllById(ordersCreateRequestDto.itemDtoList().stream()
//                .map(OrderItemDto::id)
//                .collect(Collectors.toList()));

        //Order member/item setting
        Orders orders = OrdersMapper.INSTANCE.ordersCreateDtoToEntity(ordersCreateRequestDto);
        orders.setMember(member);

        //Orders save
        ordersRepository.save(orders);

        List<OrderItem> orderItemList=new ArrayList<>();

        //OrderItem save
//        for(Item now : itemList){
        for(OrderItemDto orderItemDto : ordersCreateRequestDto.itemDtoList()){
            OrderItem orderItem = OrderItemMapper.INSTANCE.orderItemDtoToEntity(orderItemDto);
            Item item = itemRepository.findById(orderItem.getId()).orElseThrow(EntityNotFoundException::new);
            orderItem.setCreateOrderItem(item, orders);
            orderItemList.add(orderItem);
            orderItemRepository.save(orderItem);
        }

        return OrdersMapper.INSTANCE.ordersToCreateDto(orders, orderItemList);
    }

    /**
     * 주문 제거
     */
    public void deleteOrders(Long id){
        Orders orders = ordersRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문 ID 입니다."));

        ordersRepository.delete(orders);
    }
}
