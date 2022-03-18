package com.antake.code;

/**
 * @author Antake
 * @date 2020/5/7
 * @description this is description
 */
public enum BaseResponseCodeEnum implements ResponseCode{
    SUCCESS(996200,"操作成功"),
    FAIL(9962001,"操作失败"),
    SYSTEM_ERROR(996500,"系统异常"),
    SYSTEM_RUNTIME_ERROR(996501,"系统运行时异常"),
    MYBATIS_ERROR(996502,"数据源异常"),
    MYBATIS_ERROR_TOOMANYRESULTS(996503,"多个相同数据"),
    USER_UNAUTHORIZED(996403,"没有权限执行该操作"),
    USER_UNAUTHENTICATED(996401,"未登录"),
    USER_INCORRECT_CREDENTIALS(996402,"账号或密码错误"),
    USER_CONCURRENT_ACCESS(996405,"账户在多个地点登录"),
    USER_UNKNOWN_ACCOUNT(996406,"账号不存在"),
    USER_EXCESSIVE_ATTEMPTS(996407,"账号认证次数超过限制"),
    USER_DISABLED_ACCOUNT(996408,"账号已被禁用"),
    USER_LOCKED_ACCOUNT(996409,"账号已被锁定"),
    USER_UNSUPPORTED_TOKEN(996410,"使用了不支持的Token"),
    USER_NO_ACCOUNT(996411,"账号不存在"),
    USER_ACCOUNT_BLOCKED(996412,"账号已被冻结"),
    USER_ACCOUNT_ERROR(996413,"账号状态异常"),
    USER_NO_TOKEN(996401,"认证token为空,请先登录"),
    USER_ERROR_TOKEN(996401,"token认证失败"),
    WEB_MISSING_SERVLET_REQUEST_PARAMETER(996601,"请求参数异常"),
    METHOD_ILLEGAL_ARGUMENT(996701,"方法参数异常"),
    AUTH_CODE_TIMEOUT(996414,"验证码过期或不存在"),
    AUTH_CODE_ERROR(996414,"验证码错误"),
    ;

    BaseResponseCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
    * @description 响应状态码
    */
    private final int code;
    /**
    * @description 提示信息
    */
    private final String msg;


    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
