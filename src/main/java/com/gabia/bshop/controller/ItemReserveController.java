package com.gabia.bshop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.bshop.dto.response.ItemReservationResponse;
import com.gabia.bshop.security.Login;
import com.gabia.bshop.service.ItemReserveService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ItemReserveController {

	private final ItemReserveService itemReserveService;

	@Login(admin = true)
	@GetMapping("/reservations/{itemId}")
	public ResponseEntity<ItemReservationResponse> findItemReservation(@PathVariable final Long itemId) {
		return ResponseEntity.ok(itemReserveService.findItemReservation(itemId));
	}

	@Login(admin = true)
	@PostMapping("/reservations/{itemId}")
	public ResponseEntity<ItemReservationResponse> createReservation(@PathVariable final Long itemId) {
		return ResponseEntity.ok().body(itemReserveService.createItemReservation(itemId));
	}

	@Login(admin = true)
	@DeleteMapping("/reservations/{itemId}")
	public ResponseEntity<Void> deleteReservation(@PathVariable final Long itemId) {
		itemReserveService.removeReservation(itemId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
