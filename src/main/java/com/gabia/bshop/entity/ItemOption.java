package com.gabia.bshop.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@ToString(exclude = {"item"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
	name = "options",
	indexes = {})
@Entity
public class ItemOption extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@Column(columnDefinition = "varchar(255)", nullable = false)
	private String description;

	@Column(nullable = false)
	private int optionLevel;

	@Column(nullable = false)
	private int optionPrice;

	@Column(nullable = false)
	private int stockQuantity;

	@Builder
	private ItemOption(
		final Long id,
		final Item item,
		final String description,
		final int optionLevel,
		final int optionPrice,
		final int stockQuantity) {
		this.id = id;
		this.item = item;
		this.description = description;
		this.optionLevel = optionLevel;
		this.optionPrice = optionPrice;
		this.stockQuantity = stockQuantity;
	}

	public void decreaseStockQuantity(final int orderCount) {
		this.stockQuantity -= orderCount;
	}

	public void increaseStockQuantity(final int orderCount) {
		this.stockQuantity += orderCount;
	}

	@Override
	public boolean equals(final Object that) {
		if (this == that) {
			return true;
		}
		if (that == null || getClass() != that.getClass()) {
			return false;
		}
		final ItemOption itemOption = (ItemOption)that;
		return getId().equals(itemOption.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
