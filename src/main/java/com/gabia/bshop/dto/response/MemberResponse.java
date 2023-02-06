package com.gabia.bshop.dto.response;

import lombok.Builder;

@Builder
public record MemberResponse(
        Long id,
        String email,
        String name,
        String hiworksId
) {

}
