package com.antake.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author Antake
 * @date 2020/5/7
 * @description this is description
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtTokenSetting {
    private  String securityKey;
    private  Duration accessTokenExpireTime;
    private  Duration refreshTokenExpireTime;
    private  Duration refreshTokenExpireAppTime;
    private  String issuer;
}

