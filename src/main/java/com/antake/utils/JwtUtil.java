package com.antake.utils;

import com.alibaba.druid.util.StringUtils;
import com.antake.config.JwtTokenSetting;
import com.antake.constants.Constant;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

/**
 * @author Antake
 * @date 2020/5/7
 * @description this is description
 */
@Slf4j
public class JwtUtil {
    private static String securityKey;
    private static Duration accessTokenExpireTime;
    private static Duration refreshTokenExpireTime;
    private static Duration refreshTokenExpireAppTime;
    private static String issuer;
    public static void setJwtProperties(JwtTokenSetting jwtTokenSetting){
        securityKey=jwtTokenSetting.getSecurityKey();
        accessTokenExpireTime=jwtTokenSetting.getAccessTokenExpireTime();
        refreshTokenExpireTime=jwtTokenSetting.getRefreshTokenExpireTime();
        refreshTokenExpireAppTime=jwtTokenSetting.getRefreshTokenExpireAppTime();
        issuer=jwtTokenSetting.getIssuer();
    }
    /**
    * @Description: 获取accessToken
    * @Param: [subject, claims]
    * @return: java.lang.String
    * @Author: MiZiMi
    * @Date: 2020/5/7 17:55
    */
    public static String getAccessToken(String subject,Map<String,Object> claims){
        return generateToken(issuer,subject,claims,accessTokenExpireTime.toMillis(),securityKey);
    }
    /**
    * @Description: 刷新token
    * @Param: [subject, claims]
    * @return: java.lang.String
    * @Author: MiZiMi
    * @Date: 2020/5/7 17:48
    */
    public static String getRefreshToken(String subject,Map<String,Object> claims){
        return generateToken(issuer,subject,claims,refreshTokenExpireTime.toMillis(),securityKey);
    }
    /**
    * @Description: 刷新appToken
    * @Param: [subject, claims]
    * @return: java.lang.String
    * @Author: MiZiMi
    * @Date: 2020/5/7 17:56
    */
    public static String getRefreshAppToken(String subject,Map<String,Object> claims){
        return generateToken(issuer,subject,claims,refreshTokenExpireAppTime.toMillis(),securityKey);
    }
    /**
     * @Description: 生成token
     * @Param: [issuer, subject, claims, ttlMillis, secret]
     * @return: java.lang.String
     * @Author: MiZiMi
     * @Date: 2020/5/7 17:47
     */
    public static String generateToken(String issuer, String subject, Map<String,Object> claims,long ttlMillis,String secret){
        SignatureAlgorithm signatureAlgorithm=SignatureAlgorithm.HS256;
        long nowMillis=System.currentTimeMillis();
        Date now=new Date(nowMillis);
        byte[] signingKey = DatatypeConverter.parseBase64Binary(secret);
        JwtBuilder builder = Jwts.builder();
        builder.setHeaderParam("typ","JWT");
        if (claims!=null){
            builder.setClaims(claims);
        }
        if (!StringUtils.isEmpty(subject)){
            builder.setSubject(subject);
        }
        if (!StringUtils.isEmpty(issuer)){
            builder.setIssuer(issuer);
        }
        builder.setIssuedAt(now);
        if (ttlMillis>=0){
            long expMillis=nowMillis+ttlMillis;
            Date exp=new Date(expMillis);
            builder.setExpiration(exp);
        }
        builder.signWith(signatureAlgorithm,signingKey);
        return builder.compact();
    }
    /**
    * @Description: 解析token
    * @Param: [token]
    * @return: io.jsonwebtoken.Claims
    * @Author: MiZiMi
    * @Date: 2020/5/7 18:03
    */
    public static Claims getClaimsFromToken(String token){
        Claims claims=null;
        try {
            claims=Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(securityKey)).parseClaimsJws(token).getBody();
        }catch (Exception e){
            if(e instanceof ClaimJwtException){
                claims=((ClaimJwtException) e).getClaims();
            }
        }
        return claims;
    }
    /**
    * @Description: 通过token获取userId
    * @Param: [token]
    * @return: java.lang.String
    * @Author: MiZiMi
    * @Date: 2020/5/7 18:08
    */
    public static String getUserId(String token){
        String userId=null;
        try {
            Claims claims=getClaimsFromToken(token);
            userId=claims.getSubject();
        }catch (Exception e){
            log.error("error={}",e);
        }
        return userId;
    }
    /**
    * @Description: 通过token获取username
    * @Param: [token]
    * @return: java.lang.String
    * @Author: MiZiMi
    * @Date: 2020/5/7 18:51
    */
    public static String getUsername(String token){
        String username=null;
        try {
            Claims claims=getClaimsFromToken(token);
            username= (String) claims.get(Constant.JWT_USER_NAME);
        }catch (Exception e){
            log.error("error={}",e);
        }
        return username;
    }
    /**
    * @Description: 验证token是否过期
    * @Param: [token]
    * @return: java.lang.Boolean
    * @Author: MiZiMi
    * @Date: 2020/5/7 18:51
    */
    public static Boolean isTokenExpired(String token){
        try{
            Claims claims=getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        }catch (Exception e){
            log.error("error={}",e);
            return true;
        }
    }
    /**
    * @Description: 验证token是否有效
    * @Param: [token]
    * @return: java.lang.Boolean
    * @Author: MiZiMi
    * @Date: 2020/5/7 18:54
    */
    public static Boolean validateToken(String token){
        Claims claimsFromToken=getClaimsFromToken(token);
        return (null!=claimsFromToken && !isTokenExpired(token));
    }
    /**
    * @Description: 获取token过期时间
    * @Param: [token]
    * @return: long
    * @Author: MiZiMi
    * @Date: 2020/5/7 18:58
    */
    public static long gerRemainingTime(String token){
        long result=0L;
        try {
            long nowMillis=System.currentTimeMillis();
            result=getClaimsFromToken(token).getExpiration().getTime()-nowMillis;
        }catch (Exception e){
            log.error("error={}",e);
        }
        return result;
    }
    /**
    * @Description: 从请求里面获取token
    * @Param: []
    * @return: java.lang.String
    * @Author: MiZiMi
    * @Date: 2020/5/11 11:27
    */
    public static String getTokenFromRequest(){
        String token=null;
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes!=null){
            HttpServletRequest request = requestAttributes.getRequest();
            token = request.getHeader(Constant.JWT_ACCESS_TOKEN);
        }
        return token;
    }
    /**
    * @Description: 通过当钱线程拿到request对象
    * @Param: []
    * @return: javax.servlet.http.HttpServletRequest
    * @Author: MiZiMi
    * @Date: 2020/5/11 15:09
    */
    public static HttpServletRequest getRequest(){
        HttpServletRequest request=null;
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes!=null){
            request=requestAttributes.getRequest();
        }
        return request;
    }
    /**
    * @Description: 获取token过期时间
    * @Param: 
    * @return: 
    * @Author: MiZiMi
    * @Date: 2020/5/11 11:28
    */
    public static long  getTokenExpireTime(){
        return accessTokenExpireTime.toMillis();
    }
}

