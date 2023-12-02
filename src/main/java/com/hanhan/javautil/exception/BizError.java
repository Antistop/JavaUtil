package com.hanhan.javautil.exception;

public enum BizError implements Errors {
    SYSTEM_ERROR(-9999, "未知错误,请稍后重试:{0}"),
    DB_EXCEPTION(-1, "数据库异常请重试"),
    DB_DUPLICATE(-2, "数据库中数据已存在"),
    PARAM_ERROR(-3, "参数异常:{0}")
    ;
    private final int code;
    private final String msg;

    BizError(int code, String msg) {
        this.code = code;
        this.msg = msg;
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
        return name();
    }
}
