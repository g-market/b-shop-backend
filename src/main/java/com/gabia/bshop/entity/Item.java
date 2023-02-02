package com.gabia.bshop.entity;

import com.gabia.bshop.entity.enumtype.ItemStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

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

    @Column(columnDefinition = "varchar(1000)", nullable = false)
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

    @Builder
    private Item(
            final Long id,
            final String name,
            final Category category,
            final String description,
            final int basePrice,
            final ItemStatus itemStatus,
            final LocalDateTime openAt,
            final boolean deleted) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.basePrice = basePrice;
        this.itemStatus = itemStatus;
        this.openAt = openAt;
        this.deleted = deleted;
    }

    public void addCategory(final Category category) {
        this.category = category;
    }

    public void update(
            final String name,
            final Category category,
            final String description,
            final int basePrice,
            final ItemStatus itemStatus,
            final LocalDateTime openAt) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.basePrice = basePrice;
        this.itemStatus = itemStatus;
        this.openAt = openAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return getId().equals(item.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
