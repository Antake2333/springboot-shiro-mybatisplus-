package com.antake.excepitions;

import com.antake.code.ResponseCode;

/**
 * @author Antake
 */
public class BusinessException extends RuntimeException {

    /**
     * 提示编码
     */
    private final  int code;

    /**
     * 后端提示语
     */
    private final String msg;

    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(ResponseCode responseCode) {
        this(responseCode.getCode(),responseCode.getMsg());
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
