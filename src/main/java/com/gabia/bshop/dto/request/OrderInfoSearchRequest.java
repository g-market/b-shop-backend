package com.gabia.bshop.dto.request;

import java.time.LocalDateTime;

public record OrderInfoSearchRequest(LocalDateTime startAt, LocalDateTime endAt) {

}
