package com.gabia.bshop.entity;

import java.util.List;
import java.util.Objects;

import com.gabia.bshop.entity.enumtype.OrderStatus;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
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

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems;

	@Builder
	private Orders(
		final Long id, final Member member, final OrderStatus status, final long totalPrice,
		final List<OrderItem> orderItems) {
		this.id = id;
		this.member = member;
		this.status = status;
		this.totalPrice = totalPrice;
		this.orderItems = orderItems;
	}

	public void createOrder(List<OrderItem> orderItemEntityList) {
		this.orderItems = orderItemEntityList;
	}

	public void calculateTotalPrice(final OrderItem orderItem, final int count) {
		this.totalPrice += orderItem.getPrice() * count;
	}

	public void cancel() {
		checkOrderStatus();
		this.orderItems.forEach(OrderItem::cancel);
		this.status = OrderStatus.CANCELLED;
	}

	public void checkOrderStatus() {
		if (this.status.equals(OrderStatus.COMPLETED)) {
			throw new IllegalStateException("상품의 상태가 완료된 상태입니다.");
		} else if (this.status.equals(OrderStatus.CANCELLED)) {
			throw new IllegalStateException("상품이 이미 취소된 상태입니다.");
		}
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final Orders orders = (Orders)o;
		return getId().equals(orders.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
