package com.gabia.bshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.gabia.bshop.entity.Order;

import jakarta.persistence.LockModeType;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

	Optional<Order> findByIdAndMemberId(Long orderId, Long memberId);

	@Query("""
		select o from Order o
		join fetch o.orderItemList
		where o.id = :orderId
		and o.member.id = :memberId
		""")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<Order> findByIdAndMemberIdWithLock(Long orderId, Long memberId);

	List<Order> findAllByMemberId(Long memberId);
}
