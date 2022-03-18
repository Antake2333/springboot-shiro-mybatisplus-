package com.antake.shiro;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;

import com.antake.code.BaseResponseCodeEnum;
import com.antake.components.RedisUtil;
import com.antake.constants.Constant;
import com.antake.excepitions.BusinessException;
import com.antake.utils.DataResult;
import com.antake.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.http.MediaType;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Antake
 * @date 2020/5/8
 * @description 自定义token过滤器
 */
@Slf4j
public class CustomAccessControlFilter extends AccessControlFilter {
    /**
    * @Description: 如果是true就流转到下一个链式调用，如果返回false就会流转到onAccessDenied
    * @Param: [servletRequest, servletResponse, o]
    * @return: boolean
    * @Author: MiZiMi
    * @Date: 2020/5/8 13:35
    */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }
    /**
    * @Description: 如果返回true就会流转到下一个链式调用，false就不会
    * @Param: [servletRequest, servletResponse]
    * @return: boolean
    * @Author: MiZiMi
    * @Date: 2020/5/8 13:37
    */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        log.info("request 接口地址：{}",request.getRequestURI());
        log.info("request 接口的请求方式：{}",request.getMethod());
        String accessToken=request.getHeader(Constant.JWT_ACCESS_TOKEN);
        try {
            if (StringUtils.isEmpty(accessToken)){
                customResponse(servletResponse,DataResult.getResult(BaseResponseCodeEnum.USER_NO_TOKEN));
                return false;
            }
            WebApplicationContext requiredWebApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            if (requiredWebApplicationContext!=null){
                RedisUtil redisUtil = requiredWebApplicationContext.getBean("redisUtil", RedisUtil.class);
                if (redisUtil!=null){
                    //校验用户是否登录成功
                    boolean hasLogin = redisUtil.hasKey(Constant.USER_ONLINE_KEY + JwtUtil.getUserId(JwtUtil.getTokenFromRequest()) + "-" + accessToken);
                    if (!hasLogin){
                        customResponse(servletResponse,DataResult.getResult(BaseResponseCodeEnum.USER_UNAUTHENTICATED));
                        return false;
                    }
                }
            }
            CustomUsernamePasswordToken customUsernamePasswordToken = new CustomUsernamePasswordToken(accessToken);
            getSubject(servletRequest,servletResponse).login(customUsernamePasswordToken);
        } catch (BusinessException e) {
            customResponse(servletResponse,DataResult.getResult(e.getCode(),e.getMsg()));
            return false;
        } catch (AuthenticationException e) {
            if(e.getCause() instanceof BusinessException){
                BusinessException exception= (BusinessException) e.getCause();
                customResponse(servletResponse,DataResult.getResult(exception.getCode(),exception.getMsg()));
            }else {
                customResponse(servletResponse,DataResult.getResult(BaseResponseCodeEnum.USER_ERROR_TOKEN));
            }
            return false;
        } catch (Exception e){
            log.error("onAccessDenied...error{}",e);
            customResponse(servletResponse,DataResult.getResult(BaseResponseCodeEnum.SYSTEM_ERROR));
            return false;
        }
        return true;
    }
    /**
    * @Description: 自定义响应前端方法
    * @Param: [servletResponse, code, msg]
    * @return: void
    * @Author: MiZiMi
    * @Date: 2020/5/8 14:04
    */
    private void customResponse(ServletResponse servletResponse,DataResult result){
        servletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        servletResponse.setCharacterEncoding("UTF-8");
        ServletOutputStream outputStream = null;
        try {
            outputStream = servletResponse.getOutputStream();
            outputStream.write(JSON.toJSONString(result).getBytes("UTF-8"));
            outputStream.flush();
        } catch (IOException e) {
            log.error("customResponse...error{}", e);
        }finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                log.error("customResponse...outputStream...close...error{}", e);
            }
        }
    }
}

