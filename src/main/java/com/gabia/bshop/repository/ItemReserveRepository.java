package com.gabia.bshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabia.bshop.entity.Reservation;

public interface ItemReserveRepository extends JpaRepository<Reservation, Long> {

	Optional<Reservation> findByItem_Id(final Long itemId);
}
