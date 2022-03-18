package com.antake.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Antake
 * @date 2020/5/13
 * @description this is description
 */
@Data
@ApiModel(value = "分页实体类")
public class PageVO implements Serializable {
    @ApiModelProperty("当前页")
    @NotNull(message = "当前页不能为空")
    private Long page;
    @ApiModelProperty("每页显示条数")
    @NotNull(message = "每页显示条数不能为空")
    private Long limit;
    @ApiModelProperty("排序类型")
    @NotBlank(message = "排序类型不能为空")
    private String sort;
    @ApiModelProperty("查询条件")
    private String condition;
}

