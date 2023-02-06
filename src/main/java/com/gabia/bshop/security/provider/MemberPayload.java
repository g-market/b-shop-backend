package com.gabia.bshop.security.provider;

import com.gabia.bshop.entity.enumtype.MemberRole;
import lombok.Builder;

@Builder
public record MemberPayload(Long id, MemberRole role) {

    public boolean isAdmin() {
        return role == MemberRole.ADMIN;
    }
}
