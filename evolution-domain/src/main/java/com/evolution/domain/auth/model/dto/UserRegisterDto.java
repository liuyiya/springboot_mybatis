package com.evolution.domain.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {

    /**
     * 电话
     */
    @NotNull
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String nickName;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 备注
     */
    private String remark;

}
