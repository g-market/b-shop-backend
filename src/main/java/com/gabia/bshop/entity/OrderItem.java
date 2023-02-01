package com.gabia.bshop.entity;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = {"item", "order"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_item", indexes = {})
@Entity
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @Column(nullable = false)
    private int orderCount;

    @Column(nullable = false)
    private long price;

    @Builder
    private OrderItem(final Long id, final Item item, final Orders order, final int orderCount, final long price) {
        this.id = id;
        this.item = item;
        this.order = order;
        this.orderCount = orderCount;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderItem orderItem = (OrderItem) o;
        return getId().equals(orderItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}