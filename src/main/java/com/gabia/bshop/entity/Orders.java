package com.gabia.bshop.entity;

import java.util.Objects;

import com.gabia.bshop.entity.enumtype.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = {"member"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
	name = "orders",
	indexes = {})
@Entity
public class Orders extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Enumerated(value = EnumType.STRING)
	@Column(columnDefinition = "char(9)", nullable = false)
	private OrderStatus status;

	@Column(nullable = false)
	private long totalPrice;

	@Builder
	private Orders(
		final Long id, final Member member, final OrderStatus status, final long totalPrice) {
		this.id = id;
		this.member = member;
		this.status = status;
		this.totalPrice = totalPrice;
	}

	@Override
	public boolean equals(final Object that) {
		if (this == that) {
			return true;
		}
		if (that == null || getClass() != that.getClass()) {
			return false;
		}
		final Orders orders = (Orders)that;
		return getId().equals(orders.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
