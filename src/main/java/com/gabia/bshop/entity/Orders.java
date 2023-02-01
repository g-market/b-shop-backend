package com.gabia.bshop.entity;

import com.gabia.bshop.entity.enumtype.OrderStatus;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = {"member"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "orders",
        indexes = {})
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "char(9)", nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private long totalPrice;

    @Builder
    private Orders(
            final Long id, final Member member, final OrderStatus status, final long totalPrice) {
        this.id = id;
        this.member = member;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Orders orders = (Orders) o;
        return getId().equals(orders.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
