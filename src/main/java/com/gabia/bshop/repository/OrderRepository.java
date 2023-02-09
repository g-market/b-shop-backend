package com.gabia.bshop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gabia.bshop.entity.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {

	// TODO: created_at 인덱스 생성
	@Query("select o from Orders o where o.member.id =:memberId order by o.createdAt")
	List<Orders> findByMemberIdPagination(Long memberId, Pageable pageable);
}
