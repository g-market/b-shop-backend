package com.gabia.bshop.repository;

import com.gabia.bshop.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

}
