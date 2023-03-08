package com.gabia.bshop.entity;

import java.util.Objects;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.gabia.bshop.dto.request.CategoryUpdateRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Audited
@ToString(exclude = {})
@Getter
@SQLDelete(sql = "update category set deleted = true where id = ?")
@Where(clause = "deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category", indexes = {})
@Entity
public class Category extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String name;

	@Column(nullable = false)
	private boolean deleted;

	@Builder
	private Category(final Long id, final String name) {
		this.id = id;
		this.name = name;
		this.deleted = false;
	}

	public void update(final CategoryUpdateRequest categoryUpdateRequest) {
		updateName(categoryUpdateRequest.name());
	}

	private void updateName(final String name) {
		if (name != null) {
			this.name = name;
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
		Category category = (Category)that;
		return getId().equals(category.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
