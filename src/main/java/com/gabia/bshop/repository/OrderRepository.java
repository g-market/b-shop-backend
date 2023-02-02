package com.gabia.bshop.repository;

import com.gabia.bshop.entity.OrderItem;
import com.gabia.bshop.entity.Orders;
import java.awt.print.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    // TODO: created_at 인덱스 생성
    @Query("select o from Orders o where o.member.id =: memberId order by o.createdAt")
    List<Orders> findByMemberIdPagination(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select oi.id, i.id, i.name, ii.url, count(i.id)"
            + "from OrderItem oi "
            + "inner join oi.item i "
            + "inner join ItemImage ii "
            + "on i.id = ii.item.id "
            + "where oi.id in (:orderIds)")
    List<OrderItem> test1(@Param("orderIds") List<Long> orderIds);

    // 총 주문개수
    // 아이템 이미지
    // 아이템 제목
}
