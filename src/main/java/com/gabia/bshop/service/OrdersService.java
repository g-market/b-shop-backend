package com.gabia.bshop.service;

import com.gabia.bshop.dto.OrdersCreateRequestDto;
import com.gabia.bshop.dto.OrdersCreateResponseDto;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.entity.Options;
import com.gabia.bshop.mapper.OrdersMapper;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.response.OrderInfoPageResponse;
import com.gabia.bshop.entity.ItemImage;
import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.Orders;
import com.gabia.bshop.mapper.OrderInfoMapper;
import com.gabia.bshop.repository.ItemImageRepository;
import com.gabia.bshop.mapper.OrdersMapper;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.OptionsRepository;
import com.gabia.bshop.repository.OrderItemRepository;
import com.gabia.bshop.repository.OrdersRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrdersService {

	private final OrdersRepository ordersRepository;
	private final OrderItemRepository orderItemRepository;
	private final ItemImageRepository itemImageRepository;
	private final MemberRepository memberRepository;
    private final OptionsRepository optionsRepository;

	@Transactional(readOnly = true)
	public OrderInfoPageResponse findOrdersPagination(final Long memberId, final Pageable pageable) {
		memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException("해당하는 id의 회원이 존재하지 않습니다."));

		final List<Orders> orders = ordersRepository.findByMemberIdPagination(memberId, pageable);
		final List<OrderItem> orderItems = orderItemRepository.findByOrderIds(
			orders.stream().map(o -> o.getId()).collect(Collectors.toList()));
		final List<ItemImage> itemImagesWithItem = itemImageRepository.findWithItemByItemIds(
			orderItems.stream().map(oi -> oi.getItem().getId()).collect(Collectors.toList()));

		return OrderInfoMapper.INSTANCE.orderInfoRelatedEntitiesToOrderInfoPageResponse(orders, orderItems,
			itemImagesWithItem);
	}

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
