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
	name = "item_image",
	indexes = {})
@Entity
public class ItemImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@Column(nullable = false)
	private String url;

	@Builder
	private ItemImage(final Long id, final Item item, final String url) {
		this.id = id;
		this.item = item;
		this.url = url;
	}

	public void setItem(Item item) {
		updateItem(item);
	}

	private void updateItem(Item item) {
		if (item != null) {
			this.item = item;
		}
	}

	@Override
	public boolean equals(final Object that) {
		if (this == that) {
			return true;
		}
		if (that == null || getClass() != that.getClass()) {
			return false;
		}
		final ItemImage itemImage = (ItemImage)that;
		return getId().equals(itemImage.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
