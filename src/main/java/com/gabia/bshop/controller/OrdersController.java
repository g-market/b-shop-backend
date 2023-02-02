package com.gabia.bshop.controller;

import com.gabia.bshop.dto.OrdersDto;
import com.gabia.bshop.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    /**
     * 주문 생성
     */
    @PostMapping("/orders")
    public ResponseEntity<OrdersDto> createOrders(@RequestBody OrdersDto ordersDto){
        OrdersDto result = ordersService.createOrders(ordersDto);
        return ResponseEntity.ok().body(result);
    }

    /**
     * 주문 제거
     */
    @DeleteMapping("/orders/{id}")
    public void deleteOrders(@PathVariable Long id){
        ordersService.deleteOrders(id);
    }
}
