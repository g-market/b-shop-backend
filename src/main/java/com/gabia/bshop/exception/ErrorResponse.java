package com.gabia.bshop.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String message
) {
}
