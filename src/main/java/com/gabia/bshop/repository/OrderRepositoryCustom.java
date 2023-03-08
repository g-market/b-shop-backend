package com.gabia.bshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gabia.bshop.dto.searchConditions.OrderSearchConditions;
import com.gabia.bshop.entity.Order;

public interface OrderRepositoryCustom {

	Page<Order> findAllBySearchConditions(Pageable pageable, OrderSearchConditions orderSearchConditions,
		Long memberId);
}
