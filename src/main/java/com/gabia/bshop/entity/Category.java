package com.gabia.bshop.entity;

import java.util.Objects;

import com.gabia.bshop.dto.CategoryDto;

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

@ToString(exclude = {})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category", indexes = {})
@Entity
public class Category extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "varchar(255)", unique = true, nullable = false)
	private String name;

	@Builder
	private Category(final Long id, final String name) {
		this.id = id;
		this.name = name;
	}

	public void update(final CategoryDto categoryDto) {
		updateName(categoryDto.name());
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
