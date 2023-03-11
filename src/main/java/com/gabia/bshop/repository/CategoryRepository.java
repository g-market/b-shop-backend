package com.gabia.bshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gabia.bshop.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {

	boolean existsByName(String categoryName);

	Page<Category> findAll(Pageable page);
}
