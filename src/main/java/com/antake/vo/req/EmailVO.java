package com.antake.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Antake
 * @date 2020/5/19
 * @description this is description
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "邮件实体类")
public class EmailVO implements Serializable {
    @ApiModelProperty(value = "主题")
    @NotBlank(message = "主题不能为空")
    private String subject;
    @ApiModelProperty(value = "收件人")
    private String[] tos;
    @NotBlank(message = "内容不能为空")
    @ApiModelProperty(value = "内容")
    private String content;
}

