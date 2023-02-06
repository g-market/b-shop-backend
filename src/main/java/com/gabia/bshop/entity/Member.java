package com.gabia.bshop.entity;

import static com.gabia.bshop.entity.enumtype.MemberRole.*;

import java.util.Objects;

import com.gabia.bshop.entity.enumtype.MemberGrade;
import com.gabia.bshop.entity.enumtype.MemberRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = {})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
	name = "member",
	indexes = {})
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
	private Member(
		final Long id,
		final String email,
		final String phoneNumber,
		final String name,
		final MemberRole role,
		final MemberGrade grade,
		final String hiworksId) {
		this.id = id;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.name = name;
		this.role = role;
		this.grade = grade;
		this.hiworksId = hiworksId;
	}

	public void update(final Member updateMember) {
		updateHiworksId(updateMember.hiworksId);
		updateEmail(updateMember.email);
		updateName(updateMember.name);
	}

	public void updatePhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	// TODO: 사전에 가격을 정하던지, Grade 변경 메서드 추후 리팩터링 필요
	public void updateGrade(final MemberGrade grade) {
		if (grade != null) {
			this.grade = grade;
		}
	}

	private void updateHiworksId(final String hiworksId) {
		if (hiworksId != null) {
			this.hiworksId = hiworksId;
		}
	}

	private void updateEmail(final String email) {
		if (email != null) {
			this.email = email;
		}
	}

	private void updateName(final String name) {
		if (name != null) {
			this.name = name;
		}
	}

	public boolean isAdmin() {
		return role == ADMIN;
	}

	@Override
	public boolean equals(final Object that) {
		if (this == that) {
			return true;
		}
		if (that == null || getClass() != that.getClass()) {
			return false;
		}
		final Member member = (Member)that;
		return Objects.equals(id, member.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
