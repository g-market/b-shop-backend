package com.gabia.bshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabia.bshop.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

	Optional<Order> findByIdAndMemberId(Long orderId, Long memberId);

	List<Order> findAllByMemberId(Long memberId);
}
