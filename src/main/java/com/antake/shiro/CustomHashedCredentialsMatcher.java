package com.antake.shiro;

import com.antake.code.BaseResponseCodeEnum;
import com.antake.components.RedisUtil;
import com.antake.constants.Constant;
import com.antake.excepitions.BusinessException;
import com.antake.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Antake
 * @date 2020/5/8
 * @description this is description
 */
@Slf4j
public class CustomHashedCredentialsMatcher extends HashedCredentialsMatcher {
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        CustomUsernamePasswordToken customUsernamePasswordToken=(CustomUsernamePasswordToken)token;
        String accessToken= (String) customUsernamePasswordToken.getCredentials();
        String userId= JwtUtil.getUserId(accessToken);
        log.info("doCredentialsMatch...userId{}",userId);
        //判断用户是否被删除
        if (redisUtil.hasKey(Constant.DELEDED_USER_KEY+userId)){
            throw new BusinessException(BaseResponseCodeEnum.USER_NO_ACCOUNT);
        }
        //判断是否被锁定
        if (redisUtil.hasKey(Constant.ACCOUNT_LOCK_KEY+userId)){
            throw new LockedAccountException();
        }
        //校验token
        if (!JwtUtil.validateToken(accessToken)){
            throw new BusinessException(BaseResponseCodeEnum.USER_ERROR_TOKEN);
        }
        return true;
    }
}

