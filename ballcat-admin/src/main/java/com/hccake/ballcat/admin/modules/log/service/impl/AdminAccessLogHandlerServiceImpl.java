package com.hccake.ballcat.admin.modules.log.service.impl;

import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.hccake.ballcat.admin.modules.log.model.entity.AdminAccessLog;
import com.hccake.ballcat.admin.modules.log.thread.AccessLogAdminSaveThread;
import com.hccake.ballcat.admin.oauth.SysUserDetails;
import com.hccake.ballcat.admin.oauth.util.SecurityUtils;
import com.hccake.ballcat.commom.log.access.service.AccessLogHandlerService;
import com.hccake.ballcat.commom.log.util.LogUtils;
import com.hccake.ballcat.common.core.util.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 访问日志
 *
 * @author hccake
 * @date 2019-10-16 16:09:25
 */
@Slf4j
@Service
public class AdminAccessLogHandlerServiceImpl implements AccessLogHandlerService<AdminAccessLog> {

    @Autowired
    private AccessLogAdminSaveThread accessLogAdminSaveThread;

    /**
     * 生产一个日志
     *
     * @return accessLog
     * @param request
     * @param response
     * @param time
     * @param myThrowable
     */
    @Override
    public AdminAccessLog prodLog(HttpServletRequest request, HttpServletResponse response, Long time, Throwable myThrowable) {

        AdminAccessLog adminAccessLog = new AdminAccessLog()
                .setCreateTime(LocalDateTime.now())
                .setTime(time)
                .setIp(IPUtil.getIpAddr(request))
                .setMethod(request.getMethod())
                .setUserAgent(request.getHeader("user-agent"))
                .setUri(URLUtil.getPath(request.getRequestURI()))
                .setMatchingPattern(String.valueOf(request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE)))
                .setErrorMsg(Optional.ofNullable(myThrowable).map(Throwable::getMessage).orElse(null))
                .setHttpStatus(response.getStatus())
                .setReqParams(JSONUtil.toJsonStr(request.getParameterMap()));

        // 非文件上传请求，记录body
        if (!LogUtils.isMultipartContent(request)){
            adminAccessLog.setReqBody(LogUtils.getRequestBody(request));
        }

        // 如果登陆用户 则记录用户名和用户id
        Optional.ofNullable(SecurityUtils.getSysUserDetails())
                .map(SysUserDetails::getSysUser)
                .ifPresent(x -> {
                    adminAccessLog.setUserId(x.getUserId());
                    adminAccessLog.setUsername(x.getUsername());
                });

        return adminAccessLog;
    }


    /**
     * 记录日志
     *
     * @param accessLog
     */
    @Override
    public void saveLog(AdminAccessLog accessLog) {
        accessLogAdminSaveThread.putObject(accessLog);
    }




}
