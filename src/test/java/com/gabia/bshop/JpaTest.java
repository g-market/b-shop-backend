package com.gabia.bshop;

import com.gabia.bshop.entity.Category;
import jakarta.persistence.EntityManager;
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
        entityManager.persist(new Category(null, "test_category"));
        //when
        Category category = entityManager.find(Category.class, 1L);
        //then
        System.out.println("category = " + category);
    }
}
