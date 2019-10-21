package com.hccake.ballcat.common.core.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.hccake.ballcat.common.core.constant.HeaderConstants;
import com.hccake.ballcat.common.core.result.R;
import com.hccake.ballcat.common.core.result.ResultStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 20:28
 */
public class ActuatorFilter extends OncePerRequestFilter {

    private String secretId;
    private String secretKey;

    public ActuatorFilter(String secretId, String secretKey){
        this.secretId = secretId;
        this.secretKey = secretKey;
    }

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 检验签名是否正确
        String reqSecretId = request.getHeader(HeaderConstants.SECRET_ID);
        String sign = request.getHeader(HeaderConstants.SIGN);
        String reqTime = request.getHeader(HeaderConstants.REQ_TIME);
        if (verifySign(reqSecretId, sign, reqTime)) {
            filterChain.doFilter(request, response);
        }else {
            response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8.toString());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(JSONUtil.toJsonStr(R.failed(ResultStatus.UNAUTHORIZED)));
        }
    }

    /**
     * 校验sign
     * @param reqSecretId
     * @param sign
     * @param reqTime
     * @return
     */
    private boolean verifySign(String reqSecretId, String sign, String reqTime) {
        if (StrUtil.isNotBlank(sign) && StrUtil.isNotBlank(reqTime) && StrUtil.isNotBlank(reqSecretId)) {
            if(!reqSecretId.equals(secretId)){
                return false;
            }
            // 过期时间 30秒失效
            long expireTime = 30 * 1000;
            long nowTime = System.currentTimeMillis();
            if (nowTime - Long.parseLong(reqTime) <= expireTime) {
                String reverse = StrUtil.reverse(reqTime);
                String checkSign = SecureUtil.md5(reverse + secretId + secretKey);
                return StrUtil.equalsIgnoreCase(checkSign, sign);
            }
        }
        return false;
    }
}
