package com.evolution.domain.system.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * 角色用户关联表实体类
 */
@Data
public class SysRoleUser {

    /**
     * 主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 用户ID
     */
    private Long userId;

}

