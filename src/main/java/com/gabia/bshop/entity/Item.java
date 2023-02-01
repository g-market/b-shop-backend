package com.gabia.bshop.entity;

import com.gabia.bshop.entity.enumtype.ItemStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Getter
@SQLDelete(sql = "update item set deleted = true where id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, columnDefinition = "varchar(1000)")
    private String description;

    @Column(nullable = false)
    private int basePrice;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, columnDefinition = "char(8)")
    private ItemStatus itemStatus;

    @Column(nullable = false)
    private LocalDateTime openAt;

    @Column(nullable = false)
    private boolean deleted;

    @Builder
    private Item(final Long id, final String name, final Category category, final String description, final int basePrice,
            final ItemStatus itemStatus, final LocalDateTime openAt, final boolean deleted) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.basePrice = basePrice;
        this.itemStatus = itemStatus;
        this.openAt = openAt;
        this.deleted = deleted;
    }
}
