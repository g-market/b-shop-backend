package com.gabia.bshop.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Options extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String description;

    @Column(nullable = false)
    private int optionLevel;

    @Column(nullable = false)
    private int optionPrice;

    @Column(nullable = false)
    private int stockQuantity;
}
