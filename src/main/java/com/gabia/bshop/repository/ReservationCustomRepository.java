package com.gabia.bshop.repository;

import java.util.List;

import com.gabia.bshop.entity.enumtype.ItemStatus;

public interface ReservationCustomRepository {

	void updateAllItemStatusAndDeleteReservation(List<Long> reservationList, List<Long> itemIdList,
		ItemStatus itemStatus);
}
