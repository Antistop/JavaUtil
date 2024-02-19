package com.hanhan.javautil.result;

import com.hanhan.javautil.exception.BizException;
import com.hanhan.javautil.exception.Errors;
import java.text.MessageFormat;

public class Result<T> implements Errors {
    /**
     * 异常数字编码,全局唯一，成功为0
     * */
    private final int code;
    /**
     * 异常英文编码，全局唯一，成功为空
     * */
    private final String errorCode;
    /**
     * 服务端已格式化好的异常文案
     * */
    private final String msg;
    /**
     * 异常文案格式化参数
     * */
    private final String[] msgParam;
    /**
     * 返回的业务数据对象
     * */
    private final T data;

    public Result(T data) {
        this.code = 0;
        this.msg = null;
        this.msgParam = null;
        this.errorCode = null;
        this.data = data;
    }

    public Result(T data,String msg,String msgParam[]) {
        this.code = 0;
        this.msg = msg;
        this.msgParam = msgParam;
        this.errorCode = null;
        this.data = data;
    }

    public Result(BizException e) {
        this.code = e.getCode();
        this.msg = e.getMsg();
        this.msgParam = e.getMsgParam();
        this.errorCode = e.getErrorCode();
        this.data = null;
    }

    public Result(T data, BizException e) {
        this.code = e.getCode();
        this.msg = e.getMsg();
        this.msgParam = e.getMsgParam();
        this.errorCode = e.getErrorCode();
        this.data = data;
    }

    public Result(Errors errors) {
        this.code = errors.getCode();
        this.msg = errors.getMsg();
        this.msgParam = null;
        this.errorCode = errors.getErrorCode();
        this.data = null;
    }

    public Result(T data, Errors errors) {
        this.code = errors.getCode();
        this.msg = errors.getMsg();
        this.msgParam = null;
        this.errorCode = errors.getErrorCode();
        this.data = data;
    }

    public Result(Errors errors, Object... args) {
        this.code = errors.getCode();
        this.errorCode = errors.getErrorCode();
        this.msg = MessageFormat.format(errors.getMsg(), args);
        String[] param = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                param[i] = args[i].toString();
            }
        }
        this.msgParam = param;
        this.data = null;
    }

    public Result(T data, Errors errors, Object... args) {
        this.code = errors.getCode();
        this.msg = MessageFormat.format(errors.getMsg(), args);
        String[] param = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                param[i] = args[i].toString();
            }
        }
        this.msgParam = param;
        this.errorCode = errors.getErrorCode();
        this.data = data;
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> ok() {
        return new Result<>();
    }

    private Result() {
        this.code = 0;
        this.errorCode = null;
        this.msg = null;
        this.msgParam = null;
        this.data = null;
    }

    public static <T> Result<T> error(Errors errors) {
        return new Result<>(errors);
    }

    public static <T> Result<T> error(T data, Errors errors) {
        return new Result<>(data, errors);
    }

    public static <T> Result<T> error(Errors errors, Object... args) {
        return new Result<>(errors, args);
    }

    public static <T> Result<T> error(BizException e) {
        return new Result<>(e);
    }

    public static <T> Result<T> error(T data, BizException e) {
        return new Result<>(data, e);
    }

    public static <T> Result<T> error(T data, Errors errors, Object... args) {
        return new Result<>(data, errors, args);
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    public boolean isSuccess() {
        return code == 0;
    }

    public T getData() {
        return data;
    }

    public String[] getMsgParam() {
        return msgParam;
    }
}
