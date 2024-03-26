package com.evolution.types.exception;

import com.evolution.types.enums.ErrorCodeEnum;
import com.evolution.types.reponse.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice(basePackages = {"com.evolution.trigger.http"})
@ResponseBody
@Slf4j
public class AppExceptionHandler {

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public ResultBody<Void> MissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return new ResultBody<>(ErrorCodeEnum.system_error.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResultBody<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("请求参数错误|{}|{}", request.getRequestURI(), e.getMessage());
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        return new ResultBody<>(HttpStatus.BAD_REQUEST.value(), allErrors.get(allErrors.size() - 1).getDefaultMessage());
    }

    @ExceptionHandler(value = {AppException.class})
    public ResultBody<Void> AppException(AppException e) {
        log.error(e.getMessage(), e);
        return new ResultBody<>(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(value = {Exception.class})
    public ResultBody<Void> exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return new ResultBody<>(ErrorCodeEnum.system_error);
    }

}
