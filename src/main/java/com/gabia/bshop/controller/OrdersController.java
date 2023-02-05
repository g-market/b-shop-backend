package com.gabia.bshop.controller;

import com.gabia.bshop.dto.OrdersCreateRequestDto;
import com.gabia.bshop.dto.OrdersCreateResponseDto;
import com.gabia.bshop.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<OrdersCreateResponseDto> createOrder(
            @RequestBody OrdersCreateRequestDto ordersCreateRequestDto) {
        OrdersCreateResponseDto result = ordersService.createOrder(ordersCreateRequestDto);
        return ResponseEntity.ok().body(result);
    }

    /**
     * 주문 취소
     */
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        ordersService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
