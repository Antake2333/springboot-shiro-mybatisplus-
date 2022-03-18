package com.antake.controller;


import com.antake.annotations.EnableLog;
import com.antake.service.LogService;
import com.antake.utils.DataResult;
import com.antake.vo.req.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 系统日志 前端控制器
 * </p>
 *
 * @author antake
 * @since 2020-05-09
 */
@RestController
@RequestMapping("/log")
@Api(tags = "日志接口")
public class LogController {
    @Autowired
    private LogService logService;
    @EnableLog(value = "查询操作日志")
    @ApiOperation(value = "查询操作日志")
    @GetMapping("/{type}")
    @RequiresPermissions("monitor:log:select")
    public DataResult listLogs(@Valid PageVO pageVO,@PathVariable String type){
        return logService.listLogs(pageVO,type);
    }
    @EnableLog(value = "清空日志")
    @ApiOperation(value = "清空日志")
    @DeleteMapping("/{type}")
    @RequiresPermissions("monitor:log:delete")
    public DataResult deleteAllLog(@PathVariable("type")String type){
        return logService.deleteAllLog(type);
    }

    @EnableLog(value = "获取异常详情")
    @ApiOperation(value = "获取异常详情")
    @GetMapping("/detail/{id}")
    @RequiresPermissions("monitor:log:select")
    public DataResult getErrDetail(@PathVariable("id")Long id){
        return logService.getErrDetail(id);
    }
}

