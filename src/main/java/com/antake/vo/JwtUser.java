package com.antake.vo;

import com.antake.pojo.Permission;
import com.antake.pojo.Role;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Antake
 * @date 2020/5/11
 * @description this is description
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "JwtUser")
public class JwtUser implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "用户名")
    private String username;
    @JsonIgnore
    @ApiModelProperty(value = "密码")
    private String password;
    @JsonIgnore
    @ApiModelProperty(value = "盐")
    private String salt;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "性别")
    private Integer sex;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "上次登录时间")
    private Date loginTime;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "状态")
    private Integer status;
    @ApiModelProperty(value = "创建人id")
    private Long createId;
    @ApiModelProperty(value = "更新人id")
    private Long updateId;
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;
    @ApiModelProperty(value = "更新时间")
    private Date gmtModified;
    @ApiModelProperty(value = "角色")
    private List<String> roles;
    @ApiModelProperty(value = "权限")
    private List<Permission> permissions;
}

