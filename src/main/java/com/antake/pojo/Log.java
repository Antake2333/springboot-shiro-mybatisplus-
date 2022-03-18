package com.antake.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统日志
 * </p>
 *
 * @author antake
 * @since 2020-05-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Log对象", description="系统日志")
public class Log implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户操作")
    private String operation;

    @ApiModelProperty(value = "响应时间")
    private Long time;
    @ApiModelProperty(value = "浏览器")
    private String browser;
    @ApiModelProperty(value = "请求方法")
    private String method;

    @ApiModelProperty(value = "请求参数")
    private String params;
    @JsonIgnore
    @ApiModelProperty(value = "日志类型")
    private String type;
    @ApiModelProperty(value = "操作系统")
    private String operatingSystem;
    @ApiModelProperty(value = "IP地址")
    private String ip;
    @ApiModelProperty(value = "城市信息")
    @TableField(value = "cityInfo")
    private String cityInfo;
    @ApiModelProperty(value = "异常信息")
    private String exception;

    @ApiModelProperty(value = "创建时间")
      @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    public Log(String type,Long time) {
        this.time = time;
        this.type = type;
    }
}
