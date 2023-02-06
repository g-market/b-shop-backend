package com.gabia.bshop.util;

import com.gabia.bshop.security.MemberPayload;
import jakarta.annotation.Nullable;

public class MemberPayloadSupport {

    public static Long getLoggedInMemberId(@Nullable final MemberPayload loggedInMemberPayload) {
        if (loggedInMemberPayload == null) {
            return null;
        }
        return loggedInMemberPayload.id();
    }
}
