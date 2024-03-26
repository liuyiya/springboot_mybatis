package com.evolution.types.enums;

import lombok.Getter;

/**
 * 错误码枚举
 **/
@Getter
public enum ErrorCodeEnum {
    req_param_error(1000, "参数异常"),
    code_send_error(1100, "获取手机验证码失败"),
    unknown_error(400, "未知错误"),
    rsa_operate_error(1200, "rsa密钥操作失败"),
    search_fail(1210, "搜索失败"),
    lack_of_token(401, "用户未登录"),
    business_login_error(401, "商家登录失败"),
    refresh_token_fail(401, "刷新token失败"),
    token_non_exist(401, "token不存在"),
    token_expired(401, "token已过期"),
    operate_fail(1500, "操作失败"),
    system_error(500, "服务器繁忙,请稍后重试"),
    NO_PERMISSION(700, "没有访问权限"),
    ;

    private final int code;

    private final String msg;

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
