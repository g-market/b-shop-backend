package com.gabia.bshop.entity.enumtype;

import lombok.Getter;

@Getter
public enum OrderStatus {

    WAITING, // 처리 대기중
    ACCEPTED, // 접수됨
    COMPLETED, // 완료됨
}
