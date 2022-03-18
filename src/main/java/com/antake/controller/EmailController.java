package com.antake.controller;

import com.antake.annotations.EnableLog;
import com.antake.pojo.Email;
import com.antake.service.EmailService;
import com.antake.utils.DataResult;
import com.antake.vo.req.EmailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Antake
 * @date 2020/5/19
 * @description 邮箱管理模块
 */
@RestController
@RequestMapping("/email")
@Api(tags = "邮箱接口")
public class EmailController {
    @Autowired
    private EmailService emailService;
    @RequiresPermissions("tools:email:select")
    @GetMapping
    @EnableLog(value = "查询邮箱配置")
    @ApiOperation(value = "查询邮箱配置")
    public DataResult get(){
        return DataResult.getSuccessResult(emailService.find());
    }
    @RequiresPermissions("tools:email:update")
    @EnableLog(value = "修改邮箱配置")
    @ApiOperation(value = "修改邮箱配置")
    @PostMapping
    public DataResult update(@RequestBody @Valid Email email){
        return emailService.update(email);
    }
    @EnableLog(value = "发送邮件")
    @RequiresPermissions("tools:email:send")
    @ApiOperation(value = "发送邮件")
    @PostMapping("/send")
    public DataResult send(@RequestBody @Valid EmailVO emailVO){
        Email email = emailService.find();
        if (email==null){
            return DataResult.getFailResult("邮箱配置为空，请先配置发信邮箱");
        }
        emailService.send(emailVO,email);
        return DataResult.getSuccessResult();
    }
}

