package com.gabia.bshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gabia.bshop.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select oi from OrderItem oi "
            + "where oi.item.id "
            + "in (:orderIds)")
    List<OrderItem> findByOrderIds(List<Long> orderIds);

    @Query("select oi from OrderItem oi "
            + "join fetch oi.item "
            + "join fetch oi.order "
            + "where oi.order.id =:orderId "
            + "order by oi.item.id")
    List<OrderItem> findWithOrdersAndItemByOrderId(Long orderId);
}
