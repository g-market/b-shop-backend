package com.gabia.bshop.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.SQLDelete;

import com.gabia.bshop.dto.ItemDto;
import com.gabia.bshop.entity.enumtype.ItemStatus;

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

	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
	private List<ItemOption> itemOptionList;

	@Builder
	private Item(
		final Long id,
		final String name,
		final Category category,
		final String description,
		final int basePrice,
		final ItemStatus itemStatus,
		final LocalDateTime openAt,
		final boolean deleted,
		final List<ItemOption> itemOptionList) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.description = description;
		this.basePrice = basePrice;
		this.itemStatus = itemStatus;
		this.openAt = openAt;
		this.deleted = deleted;
		this.itemOptionList = itemOptionList;
	}

	public void update(final ItemDto itemDto, final Category category) {
		updateName(itemDto.name());
		updateCategory(category);
		updatePrice(itemDto.basePrice());
		updateDescription(itemDto.description());
		updateItemStatus(itemDto.itemStatus());
		updateOpenAt(itemDto.openAt());
	}

	private void updateName(final String name) {
		if (name != null) {
			this.name = name;
		}
	}

	private void updateCategory(Category category) {
		if (category != null) {
			this.category = category;
		}
	}

	private void updatePrice(int basePrice) {
		if ((Integer)basePrice != null) {
			this.basePrice = basePrice;
		}
	}

	private void updateDescription(String description) {
		if (description != null) {
			this.description = description;
		}
	}

	private void updateItemStatus(ItemStatus itemStatus) {
		if (itemStatus != null) {
			this.itemStatus = itemStatus;
		}
	}

	private void updateOpenAt(LocalDateTime openAt) {
		if (openAt != null) {
			this.openAt = openAt;
		}
	}

	@Override
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		}
		if (that == null || getClass() != that.getClass()) {
			return false;
		}
		Item item = (Item)that;
		return getId().equals(item.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
