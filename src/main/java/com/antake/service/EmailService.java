package com.antake.service;

import com.antake.pojo.Email;
import com.antake.utils.DataResult;
import com.antake.vo.req.EmailVO;

/**
 * @author Antake
 * @date 2020/5/19
 * @description 邮箱模块业务接口
 */
public interface EmailService {
    /**
    * @Description: 获取邮箱配置
    * @Param: []
    * @return: com.antake.utils.DataResult
    * @Author: MiZiMi
    * @Date: 2020/5/19 15:24
    */
    Email find();

    DataResult update(Email email);
    /**
    * @Description: 批量发送邮件
    * @Param: [emailVO]
    * @return: com.antake.utils.DataResult
    * @Author: MiZiMi
    * @Date: 2020/5/19 16:45
    */
    void send(EmailVO emailVO,Email email);
}
