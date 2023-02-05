package com.gabia.bshop.entity;

import com.gabia.bshop.exception.OutOfStockException;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = {"item"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "options",
        indexes = {})
@Entity
public class Options extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String description;

    @Column(nullable = false)
    private int optionLevel;

    @Column(nullable = false)
    private int optionPrice;

    @Column(nullable = false)
    private int stockQuantity;

    @Builder
    private Options(
            final Long id,
            final Item item,
            final String description,
            final int optionLevel,
            final int optionPrice,
            final int stockQuantity) {
        this.id = id;
        this.item = item;
        this.description = description;
        this.optionLevel = optionLevel;
        this.optionPrice = optionPrice;
        this.stockQuantity = stockQuantity;
    }

    public void removeStockQuantity(final int orderCount) {
        int restStock = this.stockQuantity - orderCount;
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다.(현재 재고 수량: " + this.stockQuantity + ")");
        }
        this.stockQuantity = restStock;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Options options = (Options) o;
        return getId().equals(options.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
