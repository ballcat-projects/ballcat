package com.hccake.ballcat.monitor.security;

import cn.hutool.crypto.SecureUtil;
import com.hccake.ballcat.common.core.constant.HeaderConstants;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.web.client.HttpHeadersProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/31 11:03
 */
@Component
public class AuthHeaderProvider implements HttpHeadersProvider {

    @Value("${monitor.secret-id: ballcat-monitor}")
    private String secretId;
    @Value("${monitor.secret-key: =BallCat-Monitor}")
    private String secretKey;


    @Override
    public HttpHeaders getHeaders(Instance instance) {
        HttpHeaders headers = new HttpHeaders();
        // 当前时间戳
        long reqTime = System.currentTimeMillis();
        headers.set(HeaderConstants.REQ_TIME, String.valueOf(reqTime));
        // 客户端ID
        headers.set(HeaderConstants.SECRET_ID, secretId);
        // sign
        String tempSign = new StringBuilder().append(reqTime).reverse().append(secretId).append(secretKey).toString();
        String sign = SecureUtil.md5(tempSign);
        headers.set(HeaderConstants.SIGN, sign);

        return headers;
    }
}
