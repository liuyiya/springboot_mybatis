package com.evolution.types.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {

    /**
     * 开启
     */
    ENABLE(1),

    /**
     * 禁用
     */
    DISABLE(0);

    private final Integer status;

    StatusEnum(Integer status) {
        this.status = status;
    }

}
