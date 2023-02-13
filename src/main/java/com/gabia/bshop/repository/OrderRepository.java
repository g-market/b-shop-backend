package com.gabia.bshop.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gabia.bshop.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	// TODO: created_at 인덱스 생성
	@Query("select o from Order o where o.member.id =:memberId order by o.createdAt")
	List<Order> findByMemberIdPagination(Long memberId, Pageable pageable);

    @Query("select o from Order o where o.createdAt >= :startAt and o.createdAt <= :endAt  order by o.createdAt")
    List<Order> findAllByPeriodPagination(LocalDateTime startAt, LocalDateTime endAt, Pageable pageable);
}
