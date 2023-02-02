package com.gabia.bshop.service;

import com.gabia.bshop.dto.OrdersDto;
import com.gabia.bshop.entity.Orders;
import com.gabia.bshop.mapper.OrdersMapper;
import com.gabia.bshop.repository.OrdersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;

    /**
     * 주문 생성
     */
    public OrdersDto createOrders(OrdersDto ordersDto){
        //Orders save
        Orders orders = ordersRepository.save(OrdersMapper.INSTANCE.ordersDtoToEntity(ordersDto));

        //OrderItem save


        return OrdersMapper.INSTANCE.ordersToDto(orders);
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
