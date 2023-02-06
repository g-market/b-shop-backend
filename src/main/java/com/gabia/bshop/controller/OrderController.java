package com.gabia.bshop.controller;

import com.gabia.bshop.dto.request.OrderInfoSearchRequest;
import com.gabia.bshop.dto.response.OrderInfoPageResponse;
import com.gabia.bshop.dto.response.OrderInfoSingleResponse;
import com.gabia.bshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    private final static int MAX_PAGE_ELEMENT_REQUEST_SIZE = 100;

    // TODO: 인가 적용
    @GetMapping("/order-infos")
    public ResponseEntity<OrderInfoPageResponse> orderInfoPagination(final Pageable pageable) {
        final Long memberId = 6L;

        validatePageElementSize(pageable);
        return ResponseEntity.ok(orderService.findOrdersPagination(memberId, pageable));
    }

    // TODO: 인가 적용
    @GetMapping("/order-infos/{orderId}")
    public ResponseEntity<OrderInfoSingleResponse> singleOrderInfo(@PathVariable("orderId") final Long orderId) {
        final OrderInfoSingleResponse singleOrderInfo = orderService.findSingleOrderInfo( orderId);
        return ResponseEntity.ok(singleOrderInfo);
    }

    // TODO: admin 인가
    @GetMapping("/admin/order-infos")
    public ResponseEntity<OrderInfoPageResponse> adminOrderInfos(final OrderInfoSearchRequest orderInfoSearchRequest, final Pageable pageable) {
        final OrderInfoPageResponse adminOrdersPagination = orderService.findAdminOrdersPagination(
                orderInfoSearchRequest, pageable);
        return ResponseEntity.ok(adminOrdersPagination);
    }

    private void validatePageElementSize(final Pageable pageable) {
        if (pageable.getPageSize() > MAX_PAGE_ELEMENT_REQUEST_SIZE) {
            throw new IllegalArgumentException(String.format("상품은 한 번에 %d개 까지만 조회할 수 있습니다.", MAX_PAGE_ELEMENT_REQUEST_SIZE));
        }
    }
}
