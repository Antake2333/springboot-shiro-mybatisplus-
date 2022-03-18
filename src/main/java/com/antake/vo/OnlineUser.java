package com.antake.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Antake
 * @date 2020/5/11
 * @description this is description
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "在线用户")
public class OnlineUser implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "ip地址")
    private String ip;
    @ApiModelProperty(value = "浏览器")
    private String browser;
    @ApiModelProperty(value = "所在城市")
    private String cityInfo;
    @ApiModelProperty(value = "上线时间")
    private Date onlineTime;
}

