package com.antake.vo.req;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Antake
 * @date 2020/6/6
 * @description this is description
 */
@Data
@ApiModel(value = "websocket message")
public class WebsocketMessage implements Serializable {
    JSONObject data;
    String destination;
}

