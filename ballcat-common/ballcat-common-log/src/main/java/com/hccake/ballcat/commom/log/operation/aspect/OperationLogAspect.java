package com.hccake.ballcat.commom.log.operation.aspect;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.hccake.ballcat.commom.log.operation.annotation.OperationLogging;
import com.hccake.ballcat.commom.log.operation.event.OperationLogEvent;
import com.hccake.ballcat.commom.log.operation.model.OperationLogDTO;
import com.hccake.ballcat.commom.log.util.LogUtils;
import com.hccake.ballcat.common.core.util.IPUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 18:16
 */
@Slf4j
@Aspect
@Order(0)
public class OperationLogAspect {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Around("@annotation(operationLogging)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLogging operationLogging) throws Throwable {
        Signature signature = joinPoint.getSignature();
        String strClassName = joinPoint.getTarget().getClass().getName();
        String strMethodName = signature.getName();
        log.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);

        // 获取日志
        OperationLogDTO operationLogDTO = prodOperationLog();
        operationLogDTO.setMsg(operationLogging.value());
        // 记录参数
        MethodSignature methodSignature = (MethodSignature) signature;
        operationLogDTO.setParams(getParams(joinPoint, methodSignature));
        // 开始时间
        Long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            operationLogDTO.setStatus(LogStatus.FAIL.getValue());
            throw throwable;
        }
        // 结束时间
        Long endTime = System.currentTimeMillis();
        operationLogDTO.setTime(endTime - startTime);
        // 发布事件
        publisher.publishEvent(new OperationLogEvent(operationLogDTO));

        return result;
    }


    /**
     * 操作状态
     */
    @Getter
    @AllArgsConstructor
    enum LogStatus {
        /**
         * 成功
         */
        SUCCESS(1),
        /**
         * 失败
         */
        FAIL(0);

        private int value;
    }



    /**
     * 获取方法参数
     * @param joinPoint
     * @param methodSignature
     * @return
     */
    private String getParams(ProceedingJoinPoint joinPoint, MethodSignature methodSignature) {
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        if(ArrayUtil.isEmpty(parameterNames)){
            return null;
        }
        Map<String, Object> paramsMap = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            paramsMap.put(parameterNames[i], args[i]);
        }
        return JSONUtil.toJsonStr(paramsMap);
    }


    /**
     * 根据请求生成操作日志
     * @return
     */
    private OperationLogDTO prodOperationLog() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        return new OperationLogDTO()
                .setCreateTime(LocalDateTime.now())
                .setIp(IPUtil.getIpAddr(request))
                .setMethod(request.getMethod())
                .setOperator(Objects.requireNonNull(LogUtils.getUsername()))
                .setStatus(LogStatus.SUCCESS.getValue())
                .setUserAgent(request.getHeader("user-agent"))
                .setUri(URLUtil.getPath(request.getRequestURI()));
    }




}
