package com.hccake.ballcat.commom.log.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.stream.Collectors;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 22:53
 */
@Slf4j
@UtilityClass
public class LogUtils {

    /**
     * 获取当前登陆用户名
     * @return
     */
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }


    /**
     * 获取请求体
     * @param request
     * @return
     */
    public String getRequestBody(HttpServletRequest request) {
        String body = null;
        if(!request.getMethod().equals(HttpMethod.GET.name())){
            try {
                BufferedReader reader = request.getReader();
                if(reader != null){
                    body = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                }
            } catch (Exception e) {
                log.error("读取请求体异常：", e);
            }
        }
        return body;
    }


    /**
     * 判断是否是multipart/form-data请求
     *
     * @param request
     * @return
     */
    public static boolean isMultipartContent(HttpServletRequest request) {
        if (!HttpMethod.POST.name().equals(request.getMethod().toUpperCase())) {
            return false;
        }
        //获取Content-Type
        String contentType = request.getContentType();
        return (contentType != null) && (contentType.toLowerCase().startsWith("multipart/"));
    }

}
