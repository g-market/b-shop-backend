package com.gabia.bshop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gabia.bshop.entity.Category;

public interface CategoryRepositoryCustom {

	List<String> findCategoryNames();

	Page<Category> findCategoryList(Pageable pageable);

	Page<Category> findCategoryListWithDeleted(Pageable pageable);
}
