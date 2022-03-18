package com.antake.vo.resp;

import com.antake.vo.JwtUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.Duration;

/**
 * @author Antake
 * @date 2020/5/8
 * @description this is description
 */
@Data
@ApiModel(value = "登录响应实体类")
public class LoginRespVO implements Serializable {
    @ApiModelProperty(value = "业务访问token")
    private String token;
    @ApiModelProperty(value = "业务刷新token")
    private String refreshToken;
    @ApiModelProperty(value = "JwtUser")
    private JwtUser user;
    @ApiModelProperty(value = "业务访问token过期时间")
    private Long expireTime;
}

