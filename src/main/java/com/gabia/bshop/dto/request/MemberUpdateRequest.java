package com.gabia.bshop.dto.request;

import jakarta.validation.constraints.NotNull;

public record MemberUpdateRequest(
        @NotNull(message = "핸드폰 번호를 기입해주세요.")
        String phoneNumber
) {
}
