package com.gabia.bshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabia.bshop.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
