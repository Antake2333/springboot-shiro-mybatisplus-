package com.antake.controller;

import com.alibaba.druid.util.StringUtils;
import com.antake.annotations.EnableLog;
import com.antake.code.BaseResponseCodeEnum;
import com.antake.components.RedisUtil;
import com.antake.constants.Constant;
import com.antake.service.UserService;
import com.antake.utils.DataResult;
import com.antake.utils.JwtUtil;
import com.antake.vo.resp.LoginRespVO;
import com.wf.captcha.ArithmeticCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Antake
 * @date 2020/5/10
 * @description this is description
 */
@RestController
@RequestMapping("/auth")
@Api(tags = "系统接口")
public class AuthController {
    @Value("${code.expiration}")
    private Long codeExpiration;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserService userService;
    @GetMapping("/code")
    @ApiOperation(value = "获取验证码")
    public DataResult getCode(){
        //算术类型的验证码
        ArithmeticCaptcha captcha=new ArithmeticCaptcha(111,36);
        //几位数运算，默认两位
        captcha.setLen(2);
        String result=captcha.text();
        String uuid= Constant.AUTH_CODE_KEY.concat(UUID.randomUUID().toString());
        //将验证码存入redis
        redisUtil.set(uuid,result,codeExpiration,TimeUnit.MINUTES);
        Map<String,Object> imgResult=new HashMap<String,Object>(2){
            {
                put("img",captcha.toBase64());
                put("uuid",uuid);
            }
        };
        return DataResult.getSuccessResult(imgResult);
    }
    @PostMapping("/refreshToken/{loginType}")
    @EnableLog(value = "刷新token")
    @ApiOperation(value = "刷新token")
    public DataResult refreshToken(@RequestBody String refreshToken,@PathVariable("loginType")String loginType){
        if (StringUtils.isEmpty(refreshToken)){
            return DataResult.getResult(BaseResponseCodeEnum.USER_NO_TOKEN);
        }
        if (JwtUtil.isTokenExpired(refreshToken)){
            return DataResult.getResult(BaseResponseCodeEnum.USER_ERROR_TOKEN);
        }
        //重新签发token
        return userService.refreshToken(refreshToken,loginType);
    }
}

