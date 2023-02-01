package com.gabia.bshop;

import com.gabia.bshop.entity.Category;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JpaTest {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    @Test
    void JPA_기본_영속_조회기능() {
        //given
        Category category = Category.builder()
                .name("test_category")
                .build();
        entityManager.persist(category);
        //when
        Category findCategory = entityManager.find(Category.class, category.getId());
        //then
        Assertions.assertThat(category.getName()).isEqualTo("test_category");
    }
}
