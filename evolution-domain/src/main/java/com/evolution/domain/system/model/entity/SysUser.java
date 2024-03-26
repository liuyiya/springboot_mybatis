package com.evolution.domain.system.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 后台用户信息实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUser {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 状态(1:正常，0:禁用)
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 手机
     */
    private String username;

    /**
     * 密码盐
     */
    private String salt;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String nickName;

    private String email;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志
     */
    private Integer delFlag;

    private String icon;

}
