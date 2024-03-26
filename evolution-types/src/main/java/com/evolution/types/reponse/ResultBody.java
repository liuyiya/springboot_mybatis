package com.evolution.types.reponse;

import com.evolution.types.enums.ErrorCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ResultBody<T> implements Serializable {

    private int code;

    private String msg;

    private T data;

    public ResultBody(T data) {
        this.data = data;
    }

    public ResultBody(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultBody(ErrorCodeEnum errorCodeEnum) {
        this.code = errorCodeEnum.getCode();
        this.msg = errorCodeEnum.getMsg();
    }

    public ResultBody(ErrorCodeEnum errorCodeEnum, String msg) {
        this.code = errorCodeEnum.getCode();
        this.msg = msg;
    }

    public boolean success() {
        if (this.getCode() == 0) {
            return true;
        }
        return false;
    }

    public static <T> ResultBody<T> success(T data) {
        ResultBody<T> serverResponseEntity = new ResultBody<>();
        serverResponseEntity.setData(data);
        serverResponseEntity.setCode(0);
        return serverResponseEntity;
    }

}