package com.antake.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Antake
 * @date 2020/5/8
 * @description this is description
 */
@Data
@ApiModel(value = "登录实体类")
public class LoginVO  implements Serializable {
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;
    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty("验证码")
    private String code;
    @NotBlank(message = "验证码uuid不能为空")
    @ApiModelProperty("验证码uuid")
    private String uuid;
    @ApiModelProperty("登录类型")
    @NotBlank(message = "登录类型不能为空")
    private String type;
}

