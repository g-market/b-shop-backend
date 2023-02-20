package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.request.ReservationChangeRequest;
import com.gabia.bshop.dto.response.ItemReservationResponse;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.Reservation;
import com.gabia.bshop.exception.ConflictException;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.ItemReservationMapper;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.repository.ItemReserveRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemReserveService {
	private final ItemReserveRepository itemReserveRepository;
	private final ItemRepository itemRepository;

	@Transactional
	public ItemReservationResponse findItemReservation(final Long itemId) {
		return ItemReservationMapper.INSTANCE.reservationToResponse(findReservationByItemId(itemId));
	}

	@Transactional
	public ItemReservationResponse createItemReservation(final Long itemId) {
		final Item item = findItemById(itemId);

		final Reservation reservation = Reservation.builder().item(item).build();
		return ItemReservationMapper.INSTANCE.reservationToResponse(itemReserveRepository.save(reservation));
	}

	@Transactional
	public ItemReservationResponse updateItemReservation(final Long itemId,
		final ReservationChangeRequest reservationChangeRequest) {
		Reservation reservation = findReservationByItemId(itemId);

		final LocalDateTime openAt = reservationChangeRequest.openAt();

		//현재시점보다 이전 시점인지 validate
		if (openAt.isAfter(LocalDateTime.now())) {
			reservation.getItem().setOpenAt(openAt);
		} else {
			throw new ConflictException(RESERVATION_TIME_NOT_VALID_EXCEPTION, openAt);
		}

		return ItemReservationMapper.INSTANCE.reservationToResponse(reservation);
	}

	@Transactional
	public void removeReservation(final Long itemId) {
		final Reservation reservation = findReservationByItemId(itemId);
		itemReserveRepository.delete(reservation);
	}

	private Reservation findReservationByItemId(final Long itemId) {
		return itemReserveRepository.findByItem_Id(itemId).orElseThrow(
			() -> new NotFoundException(ITEM_RESERVATION_NOT_FOUND_EXCEPTION, itemId)
		);
	}

	private Item findItemById(final Long itemId) {
		return itemRepository.findById(itemId).orElseThrow(
			() -> new NotFoundException(ITEM_NOT_FOUND_EXCEPTION, itemId)
		);
	}

}
