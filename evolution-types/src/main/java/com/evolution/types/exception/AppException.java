package com.evolution.types.exception;

import com.evolution.types.enums.ErrorCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppException extends RuntimeException {

    private static final long serialVersionUID = 5317680961212299217L;

    /**
     * 异常码
     */
    private Integer code;

    /**
     * 异常信息
     */
    private String msg;

    public AppException(Integer code) {
        this.code = code;
    }

    public AppException(String msg) {
        this.code = 500;
        this.msg = msg;
    }

    public AppException(ErrorCodeEnum errorCodeEnum) {
        this.code = errorCodeEnum.getCode();
        this.msg = errorCodeEnum.getMsg();
    }

    public AppException(ErrorCodeEnum errorCodeEnum, String msg) {
        this.code = errorCodeEnum.getCode();
        this.msg = msg;
    }

    public AppException(Integer code, Throwable cause) {
        this.code = code;
        super.initCause(cause);
    }

    public AppException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public AppException(Integer code, String message, Throwable cause) {
        this.code = code;
        this.msg = message;
        super.initCause(cause);
    }

}
