package com.gabia.bshop.entity;

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
@Table(name = "item_image", indexes = {})
@Entity
public class ItemImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private String url;

    @Builder
    private ItemImage(final Long id, final Item item, final String url) {
        this.id = id;
        this.item = item;
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemImage itemImage = (ItemImage) o;
        return getId().equals(itemImage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}