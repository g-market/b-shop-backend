package com.gabia.bshop.repository;

import java.util.List;

import com.gabia.bshop.entity.Reservation;
import com.gabia.bshop.entity.enumtype.ItemStatus;

public interface ReservationCustomRepository {
	void deleteAllReservation(List<Reservation> reservationList);

	void updateAllItemStatus(List<Long> itemIdList, ItemStatus itemStatus);
}
