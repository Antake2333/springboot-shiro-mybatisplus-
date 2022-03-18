package com.antake.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author antake
 * @since 2020-05-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Email对象", description="")
public class Email implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "自增ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @NotBlank(message = "邮箱地址不能为空")
    @javax.validation.constraints.Email
    @ApiModelProperty(value = "邮箱地址")
    private String user;

    @ApiModelProperty(value = "发件人姓名")
    private String username;
    @NotBlank(message = "host不能为空")
    @ApiModelProperty(value = "host")
    private String host;
    @NotNull(message = "port不能为空")
    @ApiModelProperty(value = "port")
    private Integer port;
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String pass;

    @ApiModelProperty(value = "是否SSL")
    @TableField("sslEnable")
    private Boolean sslEnable;


}
