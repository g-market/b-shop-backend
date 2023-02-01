package com.gabia.bshop.entity;

import com.gabia.bshop.entity.enumtype.MemberGrade;
import com.gabia.bshop.entity.enumtype.MemberRole;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = {})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member", indexes = {})
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(255)", unique = true, nullable = false)
    private String email;

    @Column(columnDefinition = "char(11)", unique = true)
    private String phoneNumber;

    @Column(columnDefinition = "varchar(15)", nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "char(6)", nullable = false)
    private MemberRole role;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(8)", nullable = false)
    private MemberGrade grade;

    @Column(columnDefinition = "varchar(255)", unique = true, nullable = false)
    private String hiworksId;

    @Builder
    private Member(final Long id, final String email, final String phoneNumber, final String name, final MemberRole role,
            final MemberGrade grade, final String hiworksId) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.role = role;
        this.grade = grade;
        this.hiworksId = hiworksId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return getId().equals(member.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
