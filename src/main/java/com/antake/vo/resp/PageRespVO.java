package com.antake.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author Antake
 * @date 2020/5/13
 * @description this is description
 */
@Data
@ApiModel(value = "分页响应实体类")
@Accessors(chain = true)
public class PageRespVO<T> implements Serializable {
    @ApiModelProperty(value = "返回的对象")
    private List<T> items;
    @ApiModelProperty(value = "总数")
    private Long total;
}

