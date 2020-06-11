package com.hccake.ballcat.common.core.request.wrapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/16 10:14
 * Request包装类
 * 1. body重复读取
 */
@Slf4j
public class RepeatBodyRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body;

    public RepeatBodyRequestWrapper(HttpServletRequest request) {
        super(request);
        this.body = getByteBody(request);
    }

    @Override
    public BufferedReader getReader() {
        return ObjectUtils.isEmpty(body) ? null
                : new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {}

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    private static byte[] getByteBody(HttpServletRequest request) {
        byte[] body = new byte[0];
        try {
            body = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
           log.error("解析流中数据异常", e);
        }
        return body;
    }
}