package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.request.ReservationUpdateRequest;
import com.gabia.bshop.dto.response.ItemReservationResponse;
import com.gabia.bshop.entity.Item;
import com.gabia.bshop.entity.Reservation;
import com.gabia.bshop.entity.enumtype.ItemStatus;
import com.gabia.bshop.exception.ConflictException;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.ItemReservationMapper;
import com.gabia.bshop.repository.ItemRepository;
import com.gabia.bshop.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemReserveService {

	private final ReservationRepository reservationRepository;
	private final ItemRepository itemRepository;

	public ItemReservationResponse findItemReservation(final Long itemId) {
		return ItemReservationMapper.INSTANCE.reservationToResponse(findReservationByItemId(itemId));
	}

	@Transactional
	public ItemReservationResponse createItemReservation(final Long itemId,
		final ReservationUpdateRequest reservationUpdateRequest) {

		final Item item = findItemById(itemId);
		final LocalDateTime openAt = reservationUpdateRequest.openAt();

		reservationTimeValid(openAt);
		item.setOpenAt(openAt);

		item.setItemStatus(ItemStatus.RESERVED);

		final Reservation reservation = Reservation.builder().item(item).build();
		return ItemReservationMapper.INSTANCE.reservationToResponse(reservationRepository.save(reservation));
	}

	@Transactional
	public ItemReservationResponse updateItemReservation(final Long itemId,
		final ReservationUpdateRequest reservationUpdateRequest) {
		Reservation reservation = findReservationByItemId(itemId);

		final LocalDateTime openAt = reservationUpdateRequest.openAt();

		reservationTimeValid(openAt);
		reservation.getItem().setOpenAt(openAt);

		return ItemReservationMapper.INSTANCE.reservationToResponse(reservation);
	}

	@Transactional
	public void removeReservation(final Long itemId) {
		final Reservation reservation = findReservationByItemId(itemId);
		reservationRepository.delete(reservation);
	}

	private Reservation findReservationByItemId(final Long itemId) {
		return reservationRepository.findByItemId(itemId).orElseThrow(
			() -> new NotFoundException(ITEM_RESERVATION_NOT_FOUND_EXCEPTION, itemId)
		);
	}

	private Item findItemById(final Long itemId) {
		return itemRepository.findById(itemId).orElseThrow(
			() -> new NotFoundException(ITEM_NOT_FOUND_EXCEPTION, itemId)
		);
	}

	private void reservationTimeValid(final LocalDateTime openAt) {
		if (openAt.isBefore(LocalDateTime.now())) {
			throw new ConflictException(RESERVATION_TIME_NOT_VALID_EXCEPTION, openAt);
		}
	}

}
