package com.gabia.bshop.entity;

import java.util.Objects;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

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

@Audited
@ToString(exclude = {"item"})
@Getter
@SQLDelete(sql = "update item_image set deleted = true where id = ?")
@Where(clause = "deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
	name = "item_image",
	indexes = {})
@Entity
public class ItemImage extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@Column(nullable = false)
	private String imageName;

	@Column(nullable = false)
	private boolean deleted;

	@Builder
	private ItemImage(final Long id, final Item item, final String imageName) {
		this.id = id;
		this.item = item;
		this.imageName = imageName;
		this.deleted = false;
	}

	public void updateImageName(final String imageName) {
		if (imageName != null) {
			this.imageName = imageName;
		}
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
