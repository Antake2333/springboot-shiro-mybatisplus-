package com.antake.components;

import com.antake.config.JwtTokenSetting;
import com.antake.utils.JwtUtil;
import org.springframework.stereotype.Component;

/**
 * @author Antake
 * @date 2020/5/7
 * @description this is description
 */
@Component
public class InitializerUtil {
    public InitializerUtil(JwtTokenSetting jwtTokenSetting) {
        JwtUtil.setJwtProperties(jwtTokenSetting);
    }
}

