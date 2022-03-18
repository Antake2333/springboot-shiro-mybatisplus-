package com.antake.service;

import com.antake.pojo.Log;
import com.antake.utils.DataResult;
import com.antake.vo.req.PageVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author antake
 * @since 2020-05-09
 */
public interface LogService extends IService<Log> {

    DataResult listLogs(PageVO pageVO,String type);

    DataResult deleteAllLog(String type);
    @Async
    void save(ProceedingJoinPoint joinPoint, Log log,String userId,String username,String browser,String ip);
    @Async
    void loginSave(Log log);

    DataResult getErrDetail(Long id);
}
