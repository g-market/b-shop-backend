package com.gabia.bshop.repository;

import com.gabia.bshop.entity.OrderItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select oi from OrderItem oi "
            + "where oi.item.id "
            + "in (:orderIds)")
    List<OrderItem> findByOrderIds(@Param("orderIds") List<Long> orderIds);
}
