package com.gabia.bshop.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;

import com.gabia.bshop.dto.request.ItemUpdateRequest;
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

@Audited
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

	@Column(nullable = false)
	private String thumbnail;

	@Column(columnDefinition = "smallint", nullable = false)
	private int year;

	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
	@AuditJoinTable(name = "item_option_aud")
	private List<ItemOption> itemOptionList = new ArrayList<>();

	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
	@AuditJoinTable(name = "item_image_aud")
	private List<ItemImage> itemImageList = new ArrayList<>();

	@Builder
	public Item(final Long id, final Category category, final String name, final String description,
		final int basePrice, final ItemStatus itemStatus,
		final LocalDateTime openAt, final String thumbnail, final int year) {
		this.id = id;
		this.category = category;
		this.name = name;
		this.description = description;
		this.basePrice = basePrice;
		this.itemStatus = itemStatus;
		this.openAt = openAt;
		this.deleted = false;
		this.thumbnail = thumbnail;
		this.year = year;
	}

	public void update(final ItemUpdateRequest itemUpdateRequest, final Category category) {
		updateName(itemUpdateRequest.name());
		updatePrice(itemUpdateRequest.basePrice());
		updateDescription(itemUpdateRequest.description());
		setOpenAt(itemUpdateRequest.openAt());
		setItemStatus(itemUpdateRequest.itemStatus());
		updateYear(itemUpdateRequest.year());
		updateCategory(category);
	}

	private void updateName(final String name) {
		if (name != null) {
			this.name = name;
		}
	}

	private void updatePrice(final Integer basePrice) {
		if (basePrice != null) {
			this.basePrice = basePrice;
		}
	}

	private void updateDescription(final String description) {
		if (description != null) {
			this.description = description;
		}
	}

	private void updateYear(final Integer year) {
		if (year != null) {
			this.year = year;
		}
	}

	private void updateCategory(final Category category) {
		if (category != null) {
			this.category = category;
		}
	}

	public void updateThumbnail(final String thumbnail) {
		if (thumbnail != null) {
			this.thumbnail = thumbnail;
		}
	}

	public void setItemStatus(final ItemStatus itemStatus) {
		if (itemStatus != null) {
			this.itemStatus = itemStatus;
		}
	}

	public void setOpenAt(final LocalDateTime openAt) {
		if (openAt != null) {
			this.openAt = openAt;
		}
	}

	public void addItemOption(final ItemOption itemOption) {
		this.itemOptionList.add(itemOption);
	}

	public void addItemImage(final ItemImage itemImage) {
		this.itemImageList.add(itemImage);
	}

	@Override
	public boolean equals(final Object that) {
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
