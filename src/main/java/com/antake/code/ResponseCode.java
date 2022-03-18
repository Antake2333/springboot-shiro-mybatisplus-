package com.antake.code;

/**
 * @author Antake
 * @date 2020/5/7
 * @description this is description
 */
public interface ResponseCode {
    /**
    * @Description: 获取响应状态码
    * @Param: []
    * @return: int
    * @Author: MiZiMi
    * @Date: 2020/5/7 10:51
    */
    int getCode();
    /**
    * @Description: 获取响应信息
    * @Param: []
    * @return: java.lang.String
    * @Author: MiZiMi
    * @Date: 2020/5/7 10:52
    */
    String getMsg();
}
