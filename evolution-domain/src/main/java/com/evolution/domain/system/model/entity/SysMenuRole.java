package com.evolution.domain.system.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * 菜单角色关联表实体类
 */
@Data
public class SysMenuRole {

    /**
     * 主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 角色ID
     */
    private Long roleId;

}

