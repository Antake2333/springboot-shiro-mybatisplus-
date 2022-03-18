package com.antake.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author Antake
 * @date 2020/5/8
 * @description 自定义token
 */
public class CustomUsernamePasswordToken extends UsernamePasswordToken {
    private String jwtToken;

    public CustomUsernamePasswordToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public Object getPrincipal() {
        return this.jwtToken;
    }

    @Override
    public Object getCredentials() {
        return this.jwtToken;
    }
}

