package com.hccake.ballcat.commom.log.operation.aspect;

import cn.hutool.core.util.URLUtil;
import com.hccake.ballcat.commom.log.constant.LogConstant;
import com.hccake.ballcat.commom.log.operation.annotation.OperationLogging;
import com.hccake.ballcat.commom.log.operation.enums.LogStatusEnum;
import com.hccake.ballcat.commom.log.operation.event.OperationLogEvent;
import com.hccake.ballcat.commom.log.operation.model.OperationLogDTO;
import com.hccake.ballcat.commom.log.util.LogUtils;
import com.hccake.ballcat.common.core.util.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 18:16
 * 操作日志切面类
 */
@Slf4j
@Aspect
@Order(0)
public class OperationLogAspect {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Around("execution(@(@com.hccake.ballcat.commom.log.operation.annotation.OperationLogging *) * *(..)) " +
            "|| @annotation(com.hccake.ballcat.commom.log.operation.annotation.OperationLogging)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 开始时间
        long startTime = System.currentTimeMillis();

        //获取目标方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取操作日志注解 备注： AnnotationUtils.findAnnotation 方法无法获取继承的属性
        OperationLogging operationLogging = AnnotatedElementUtils.findMergedAnnotation(method, OperationLogging.class);

        // 获取操作日志 DTO
        Assert.notNull(operationLogging, "operationLogging annotation must not be null!");
        OperationLogDTO operationLogDTO = this.initOperationLog(operationLogging, joinPoint);

        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            operationLogDTO.setStatus(LogStatusEnum.FAIL.getValue());
            throw throwable;
        } finally {
            // 结束时间
            operationLogDTO.setTime(System.currentTimeMillis() - startTime);
            // 发布事件
            publisher.publishEvent(new OperationLogEvent(operationLogDTO));
        }
    }

    /**
     * 根据请求生成操作日志
     *
     * @param operationLogging 操作日志注解
     * @param joinPoint 切点
     * @return 初始化一个操作日志DTO
     */
    private OperationLogDTO initOperationLog(OperationLogging operationLogging, ProceedingJoinPoint joinPoint) {
        // 获取 Request
        HttpServletRequest request = LogUtils.getHttpServletRequest();

        return new OperationLogDTO()
                .setCreateTime(LocalDateTime.now())
                .setIp(IPUtil.getIpAddr(request))
                .setMethod(request.getMethod())
                .setOperator(Objects.requireNonNull(LogUtils.getUsername()))
                .setStatus(LogStatusEnum.SUCCESS.getValue())
                .setUserAgent(request.getHeader("user-agent"))
                .setUri(URLUtil.getPath(request.getRequestURI()))
                .setType(operationLogging.type().getValue())
                .setMsg(operationLogging.msg())
                .setParams(LogUtils.getParams(joinPoint))
                .setTraceId(MDC.get(LogConstant.TRACE_ID));
    }
}
