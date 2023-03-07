package com.gabia.bshop.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.gabia.bshop.entity.Order;

import jakarta.persistence.LockModeType;

public interface OrderRepository extends JpaRepository<Order, Long> {

	// TODO: created_at 인덱스 생성
	@Query("""
		select o
		from Order o
		where o.member.id =:memberId
		order by o.createdAt
		""")
	List<Order> findByMemberIdPagination(Long memberId, Pageable pageable);

	@Query("""
		select o
		from Order o
		where o.createdAt >= :startAt and o.createdAt <= :endAt
		order by o.createdAt
		""")
	List<Order> findAllByPeriodPagination(LocalDateTime startAt, LocalDateTime endAt, Pageable pageable);

	Optional<Order> findByIdAndMemberId(Long orderId, Long memberId);

	@Query("""
		select o from Order o
		join fetch o.orderItemList
		where o.id = :orderId
		and o.member.id in :memberId
		""")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<Order> findByIdAndMemberIdWithLock(Long orderId, Long memberId);

	List<Order> findAllByMemberId(Long memberId);
}
