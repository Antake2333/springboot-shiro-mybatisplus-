package com.antake.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.antake.annotations.EnableLog;
import com.antake.mapper.LogMapper;
import com.antake.pojo.Log;
import com.antake.service.LogService;
import com.antake.utils.DataResult;
import com.antake.utils.StringUtils;
import com.antake.vo.req.PageVO;
import com.antake.vo.resp.PageRespVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author antake
 * @since 2020-05-09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
    @Autowired
    private LogMapper logMapper;


    private boolean checkField(JSONObject jsonObject, String field) {
        return jsonObject.containsKey(field) && !com.alibaba.druid.util.StringUtils.isEmpty(String.valueOf(jsonObject.get(field)));
    }

    @Override
    @Transactional(readOnly = true)
    public DataResult listLogs(PageVO pageVO, String type) {
        QueryWrapper<Log> logQueryWrapper = new QueryWrapper<>();
        if (!com.alibaba.druid.util.StringUtils.isEmpty(pageVO.getCondition())) {
            JSONObject jsonObject = JSON.parseObject(pageVO.getCondition());
            if (checkField(jsonObject, "anyFiled")) {
                String anyFiled = jsonObject.get("anyFiled").toString();
                logQueryWrapper.like("username", anyFiled).or().like("id", anyFiled).or().like("operation", anyFiled).or().like("browser", anyFiled).or().like("cityInfo", anyFiled);
            }
            if (checkField(jsonObject, "startTime")) {
                if (!"null".equals(String.valueOf(jsonObject.get("startTime")))) {
                    logQueryWrapper.ge("gmt_create", jsonObject.get("startTime"));
                }
            }
            if (checkField(jsonObject, "endTime")) {
                if (!"null".equals(String.valueOf(jsonObject.get("endTime")))) {
                    logQueryWrapper.le("gmt_create", jsonObject.get("endTime"));
                }
            }
        }
        logQueryWrapper.eq("type", type);
        logQueryWrapper.orderByDesc("id");
        Page<Log> logPage = logMapper.selectPage(new Page<Log>(pageVO.getPage(), pageVO.getLimit()), logQueryWrapper);
        return DataResult.getSuccessResult(new PageRespVO<Log>().setItems(logPage.getRecords()).setTotal(logPage.getTotal()));
    }

    @Override
    public DataResult deleteAllLog(String type) {
        int result = logMapper.delete(new UpdateWrapper<Log>().eq("type", type));
        if (result > 0) {
            return DataResult.getSuccessResult();
        } else {
            return DataResult.getFailResult();
        }
    }

    @Override
    public void save(ProceedingJoinPoint joinPoint, Log log, String userId, String username, String browser, String operatingSystem, String ip) {
        if (log == null || joinPoint == null) {
            return;
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        EnableLog aopLog = method.getAnnotation(EnableLog.class);
        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";
        log.setMethod(methodName);
        StringBuilder params = new StringBuilder("{");
        //参数值
        Object[] argValues = joinPoint.getArgs();
        //参数名称
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        if (argValues != null) {
            for (int i = 0; i < argValues.length; i++) {
                params.append(" ").append(argNames[i]).append(": ").append(argValues[i]);
            }
        }
        params.append("}");
        log.setParams(params.toString());
        // 描述
        log.setUserId(Long.valueOf(userId));
        log.setOperation(aopLog.value());
        log.setIp(ip);
        log.setUsername(username);
        log.setOperatingSystem(operatingSystem);
        log.setBrowser(browser);
        log.setCityInfo(StringUtils.getIpArea(ip));
        logMapper.insert(log);
    }

    @Override
    public void loginSave(Log log) {
        log.setCityInfo(StringUtils.getIpArea(log.getIp()));
        logMapper.insert(log);
    }

    @Override
    @Transactional(readOnly = true)
    public DataResult getErrDetail(Long id) {
        return DataResult.getSuccessResult(logMapper.selectById(id).getException());
    }
}
