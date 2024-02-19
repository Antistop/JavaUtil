package com.hanhan.javautil.exception;

import java.text.MessageFormat;

public class BizException extends RuntimeException implements Errors {
    private static final long serialVersionUID = -8763152336479729022L;
    private final int code;
    private final String msg;
    private final String errorCode;
    private final String[] msgParam;

    private BizException(int code, String errorCode, String msg) {
        super("[" + code + "]" + msg);
        this.code = code;
        this.msg = msg;
        this.errorCode = errorCode;
        this.msgParam = null;
    }

    private BizException(int code, String errorCode, String msg, Exception e, Object... args) {
        super("[" + code + "]" + msg, e);
        this.code = code;
        this.msg = msg;
        this.errorCode = errorCode;
        String[] param = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            param[i] = String.valueOf(args[i]);
        }
        this.msgParam = param;
    }

    private BizException(int code, String errorCode, String msg, Object... args) {
        super("[" + code + "]" + msg);
        this.code = code;
        this.msg = msg;
        this.errorCode = errorCode;
        String[] param = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            param[i] = String.valueOf(args[i]);
        }
        this.msgParam = param;
    }

    private BizException(int code, String errorCode, String msg, Exception e) {
        super("[" + code + "]" + msg, e);
        this.code = code;
        this.msg = msg;
        this.errorCode = errorCode;
        this.msgParam = null;
    }

    public BizException(Errors errors) {
        this(errors.getCode(), errors.getErrorCode(), errors.getMsg());
    }

    public BizException(Errors errors, Exception e) {
        this(errors.getCode(), errors.getErrorCode(), errors.getMsg(), e);
    }

    public BizException(Errors errors, Exception e, Object... args) {
        this(errors.getCode(), errors.getErrorCode(), MessageFormat.format(errors.getMsg(), args), e, args);
    }

    public BizException(Errors errors, Object... args) {
        this(errors.getCode(), errors.getErrorCode(), MessageFormat.format(errors.getMsg(), args), args);
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public String[] getMsgParam() {
        return msgParam;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }
}
