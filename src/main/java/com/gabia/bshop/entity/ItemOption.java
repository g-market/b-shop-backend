package com.gabia.bshop.entity;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.Objects;

import com.gabia.bshop.dto.ItemOptionDto;
import com.gabia.bshop.dto.request.ItemOptionChangeRequest;
import com.gabia.bshop.exception.ConflictException;

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
	private int optionPrice;

	@Column(nullable = false)
	private int stockQuantity;

	@Builder
	private ItemOption(
		final Long id,
		final Item item,
		final String description,
		final int optionPrice,
		final int stockQuantity) {
		this.id = id;
		this.item = item;
		this.description = description;
		this.optionPrice = optionPrice;
		this.stockQuantity = stockQuantity;
	}
	private void updateDescription(ItemOptionChangeRequest changeRequest) {
		if (changeRequest.description() != null)
			this.description = changeRequest.description();
	}

	private void updateOptionPrice(ItemOptionChangeRequest changeRequest) {
		this.optionPrice = changeRequest.optionPrice();
	}


	private void updateOptionStock(ItemOptionChangeRequest changeRequest) {
		this.stockQuantity = changeRequest.optionPrice();
	}


	public void decreaseStockQuantity(final int orderCount) {
		int restStock = this.stockQuantity - orderCount;
		if (restStock < 0) {
			throw new ConflictException(ITEM_OPTION_OUT_OF_STOCK_EXCEPTION, stockQuantity);
		}
		this.stockQuantity = restStock;
	}

	public void increaseStockQuantity(final int orderCount) {
		this.stockQuantity += orderCount;
	}

	public void update(ItemOptionChangeRequest changeRequest) {
		updateDescription(changeRequest);
		updateOptionPrice(changeRequest);
		updateOptionStock(changeRequest);
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
