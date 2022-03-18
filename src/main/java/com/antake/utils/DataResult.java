package com.antake.utils;

import com.antake.code.BaseResponseCodeEnum;
import com.antake.code.ResponseCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author Antake
 * @date 2020/5/7
 * @description this is description
 */
@Data
@ApiModel(value = "响应实体")
public class DataResult<T> {
    @ApiModelProperty(value = "响应状态码")
    private int code;
    @ApiModelProperty(value = "响应信息")
    private String msg;
    @ApiModelProperty(value = "响应数据")
    private T data;

    public DataResult() {
        this.code= BaseResponseCodeEnum.SUCCESS.getCode();
        this.msg= BaseResponseCodeEnum.SUCCESS.getMsg();
        this.data=null;
    }
    public DataResult(T data) {
        this.code= BaseResponseCodeEnum.SUCCESS.getCode();
        this.msg= BaseResponseCodeEnum.SUCCESS.getMsg();
        this.data=data;
    }
    public DataResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public DataResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public DataResult(int code, T data) {
        this.code = code;
        this.data = data;
    }
    public DataResult(ResponseCode responseCode){
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
        this.data = null;
    }
    public DataResult(ResponseCode responseCode,T data){
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
        this.data = data;
    }
    public static <T>DataResult getResult(int code, String msg, T data){
        return new DataResult(code,msg,data);
    }
    public static DataResult getResult(int code,String msg){
        return new DataResult(code,msg);
    }
    public static <T>DataResult getResult(int code,T data){
        return new DataResult(code,data);
    }
    public static DataResult getResult(ResponseCode responseCode){
        return new DataResult(responseCode);
    }
    public static <T>DataResult getResult(ResponseCode responseCode,T data){
        return new DataResult(responseCode,data);
    }
    public static DataResult getSuccessResult(){
        return new DataResult();
    }
    public static <T>DataResult getSuccessResult(T data){
        return new DataResult(data);
    }
    public static DataResult getFailResult(){
        return new DataResult(BaseResponseCodeEnum.FAIL);
    }
    public static DataResult getFailResult(String message){
        return new DataResult(BaseResponseCodeEnum.FAIL.getCode(),message);
    }
}

