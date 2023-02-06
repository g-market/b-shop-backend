package com.gabia.bshop.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.SQLDelete;

import com.gabia.bshop.entity.enumtype.ItemStatus;

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

@ToString(exclude = {"category"})
@Getter
@SQLDelete(sql = "update item set deleted = true where id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
	name = "item",
	indexes = {})
@Entity
public class Item extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@Column(columnDefinition = "varchar(255)", nullable = false)
	private String name;

	@Column(columnDefinition = "text", nullable = false)
	private String description;

	@Column(nullable = false)
	private int basePrice;

	@Enumerated(value = EnumType.STRING)
	@Column(columnDefinition = "char(8)", nullable = false)
	private ItemStatus itemStatus;

	@Column(nullable = false)
	private LocalDateTime openAt;

	@Column(nullable = false)
	private boolean deleted;

	@Builder
	private Item(
		final Long id,
		final String name,
		final Category category,
		final String description,
		final int basePrice,
		final ItemStatus itemStatus,
		final LocalDateTime openAt,
		final boolean deleted) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.description = description;
		this.basePrice = basePrice;
		this.itemStatus = itemStatus;
		this.openAt = openAt;
		this.deleted = deleted;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final Item item = (Item)o;
		return getId().equals(item.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
