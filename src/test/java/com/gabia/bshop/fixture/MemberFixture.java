package com.gabia.bshop.fixture;

import com.gabia.bshop.dto.response.HiworksProfileResponse;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.entity.Member.MemberBuilder;
import com.gabia.bshop.entity.enumtype.MemberGrade;
import com.gabia.bshop.entity.enumtype.MemberRole;

public enum MemberFixture {

    JAIME("jaime@gabia.com", "01011112222", "제이미", MemberRole.NORMAL,
            MemberGrade.GOLD, "jaime"),
    SUMMER("summer@gabia.com", "01022223333", "썸머", MemberRole.NORMAL,
            MemberGrade.PLATINUM, "summer"),

    JENNA("jenna@gabia.com", "01033334444", "제나", MemberRole.NORMAL,
            MemberGrade.PLATINUM, "jenna"),

    BECKER("becker@gabia.com", "01044445555", "벡커", MemberRole.NORMAL,
            MemberGrade.PLATINUM, "becker"),

    AIDEN("aiden@gabia.com", "01055556666", "에이든", MemberRole.ADMIN,
            MemberGrade.PLATINUM, "aiden"),

    ENDO("endo@gabia.com", "01066667777", "엔도", MemberRole.ADMIN,
            MemberGrade.PLATINUM, "endo");


    private final String email;
    private final String phoneNumber;
    private final String name;
    private final MemberRole memberRole;
    private final MemberGrade memberGrade;
    private final String hiworksId;

    MemberFixture(final String email, final String phoneNumber, final String name,
            final MemberRole memberRole, final MemberGrade memberGrade, final String hiworksId) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.memberRole = memberRole;
        this.memberGrade = memberGrade;
        this.hiworksId = hiworksId;
    }

    public Member getInstance() {
        return getInstance(null);
    }

    public Member getInstance(final Long id) {
        return memberBuilder(id).build();
    }

    private MemberBuilder memberBuilder(final Long id) {
        return Member.builder()
                .id(id)
                .email(email)
                .phoneNumber(phoneNumber)
                .name(name)
                .role(memberRole)
                .grade(memberGrade)
                .hiworksId(hiworksId);
    }

    public HiworksProfileResponse hiworksProfileResponse() {
        return HiworksProfileResponse.builder()
                .hiworksId(hiworksId)
                .email(email)
                .name(name)
                .build();
    }
}
