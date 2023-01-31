package com.gabia.bshop.entity;

import com.gabia.bshop.entity.enumtype.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, columnDefinition = "char(9)")
    private OrderStatus status;

    @Column(nullable = false)
    private long totalPrice;
}
