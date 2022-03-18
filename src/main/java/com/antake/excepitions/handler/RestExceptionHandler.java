package com.antake.excepitions.handler;
import com.alibaba.druid.util.StringUtils;
import com.antake.code.BaseResponseCodeEnum;
import com.antake.excepitions.*;
import com.antake.utils.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.*;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.crypto.BadPaddingException;

/**
 * @author Antake
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    @ExceptionHandler(BadPaddingException.class)
    public DataResult handlerBadPaddingException(Exception e){
        log.error("handlerBadPaddingException....{}",e);
        return DataResult.getSuccessResult(BaseResponseCodeEnum.WEB_MISSING_SERVLET_REQUEST_PARAMETER);
    }
    @ExceptionHandler(Exception.class)
    public DataResult handlerException(Exception e){
        log.error("handlerException....{}",e);
        return DataResult.getResult(BaseResponseCodeEnum.SYSTEM_ERROR);
    }
    @ExceptionHandler(RuntimeException.class)
    public DataResult handlerRuntimeException(Exception e){
        log.error("handlerRuntimeException....{}",e);
        return DataResult.getResult(BaseResponseCodeEnum.SYSTEM_RUNTIME_ERROR);
    }
    @ExceptionHandler(BusinessException.class)
    public DataResult handlerBusinessException(BusinessException e){
        log.error("handlerBusinessException,{},{}",e.getLocalizedMessage(),e);
        return DataResult.getResult(e.getCode(),e.getMsg());
    }
    @ExceptionHandler(UnauthorizedException.class)
    public DataResult handlerUnauthorizedException(Exception e){
        log.error("handlerUnauthorizedException....{}",e);
        return DataResult.getResult(BaseResponseCodeEnum.USER_UNAUTHORIZED);
    }
    @ExceptionHandler(UnauthenticatedException.class)
    public DataResult handlerUnauthenticatedException(Exception e){
        log.error("handlerUnauthenticatedException....{}",e);
        return DataResult.getResult(BaseResponseCodeEnum.USER_UNAUTHENTICATED);
    }
    @ExceptionHandler(IncorrectCredentialsException.class)
    public DataResult handlerIncorrectCredentialsException(Exception e){
        log.error("handlerIncorrectCredentialsException....{}",e);
        return DataResult.getResult(BaseResponseCodeEnum.USER_INCORRECT_CREDENTIALS);
    }
    @ExceptionHandler(ExpiredCredentialsException.class)
    public DataResult handlerConcurrentAccessException(Exception e){
        log.error("handlerConcurrentAccessException....{}",e);
        return DataResult.getResult(BaseResponseCodeEnum.USER_CONCURRENT_ACCESS);
    }
    @ExceptionHandler(UnknownAccountException.class)
    public DataResult handlerUnknownAccountException(Exception e){
        log.error("handlerUnknownAccountException....{}",e);
        return DataResult.getResult(BaseResponseCodeEnum.USER_UNKNOWN_ACCOUNT);
    }
    @ExceptionHandler(ExcessiveAttemptsException.class)
    public DataResult handlerExcessiveAttemptsException(Exception e){
        log.error("handlerExcessiveAttemptsException....{}",e);
        return DataResult.getResult(BaseResponseCodeEnum.USER_EXCESSIVE_ATTEMPTS);
    }
    @ExceptionHandler(DisabledAccountException.class)
    public DataResult handlerDisabledAccountException(Exception e){
        log.error("handlerDisabledAccountException....{}",e);
        return DataResult.getResult(BaseResponseCodeEnum.USER_DISABLED_ACCOUNT);
    }
    @ExceptionHandler(LockedAccountException.class)
    public DataResult handlerLockedAccountException(Exception e){
        log.error("handlerLockedAccountException....{}",e);
        return DataResult.getResult(BaseResponseCodeEnum.USER_LOCKED_ACCOUNT);
    }
    @ExceptionHandler(UnsupportedTokenException.class)
    public DataResult handlerUnsupportedTokenException(Exception e){
        log.error("handlerUnsupportedTokenException....{}",e);
        return DataResult.getResult(BaseResponseCodeEnum.USER_UNSUPPORTED_TOKEN);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public DataResult handlerMissingServletRequestParameterException(Exception e){
        log.error("handlerMissingServletRequestParameterException....{}",e);
        return DataResult.getResult(BaseResponseCodeEnum.WEB_MISSING_SERVLET_REQUEST_PARAMETER);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public DataResult handlerIllegalArgumentException(Exception e){
        log.error("handlerIllegalArgumentException....{}",e);
        return DataResult.getResult(BaseResponseCodeEnum.METHOD_ILLEGAL_ARGUMENT);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public DataResult handlerMethodArgumentNotValidException(Exception e){
        log.error("handlerMethodArgumentNotValidException....{}",e);
        if (e instanceof MethodArgumentNotValidException){
            String defaultMessage = ((MethodArgumentNotValidException) e).getBindingResult().getFieldError().getDefaultMessage();
            if (!StringUtils.isEmpty(defaultMessage)){
                return DataResult.getResult(BaseResponseCodeEnum.WEB_MISSING_SERVLET_REQUEST_PARAMETER.getCode(),defaultMessage);
            }
        }
        return DataResult.getResult(BaseResponseCodeEnum.WEB_MISSING_SERVLET_REQUEST_PARAMETER);
    }
    @ExceptionHandler(MyBatisSystemException.class)
    public DataResult handlerMyBatisSystemException(Exception e){
        if (e instanceof TooManyResultsException){
            return DataResult.getResult(BaseResponseCodeEnum.MYBATIS_ERROR_TOOMANYRESULTS);
        }
        return DataResult.getResult(BaseResponseCodeEnum.MYBATIS_ERROR);
    }
}

