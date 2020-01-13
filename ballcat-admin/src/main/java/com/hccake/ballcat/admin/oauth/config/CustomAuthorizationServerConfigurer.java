package com.hccake.ballcat.admin.oauth.config;

import com.hccake.ballcat.admin.constants.SecurityConst;
import com.hccake.ballcat.admin.oauth.CustomTokenEnhancer;
import com.hccake.ballcat.admin.oauth.SysUserDetailsServiceImpl;
import com.hccake.ballcat.admin.oauth.exception.CustomWebResponseExceptionTranslator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.sql.DataSource;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/27 16:14
 * OAuth2 授权服务器配置
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class CustomAuthorizationServerConfigurer implements AuthorizationServerConfigurer {
    private final DataSource dataSource;
    private final SysUserDetailsServiceImpl sysUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final RedisConnectionFactory redisConnectionFactory;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final CustomWebResponseExceptionTranslator customWebResponseExceptionTranslator;

    /**
     * 定义资源权限控制的配置
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .authenticationEntryPoint(authenticationEntryPoint)
                .allowFormAuthenticationForClients();
    }

    /**
     * 客户端的信息服务类配置
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 启用 jdbc 方式获取客户端配置信息
        clients.jdbc(dataSource);
    }

    /**
     * 授权服务的访问路径相关配置
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        // TODO tokenService修改有效期方案
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        // access_token 的有效时长 (秒), 默认 12 小时
        tokenServices.setAccessTokenValiditySeconds(60*60*2);
        //refresh_token 的有效时长 (秒), 默认 30 天
        tokenServices.setRefreshTokenValiditySeconds(60*60*3);

        endpoints.tokenStore(tokenStore())
                .userDetailsService(sysUserDetailsService)
                .authenticationManager(authenticationManager)
                // 自定义token
                .tokenEnhancer(tokenEnhancer())
                // 强制刷新token时，重新生成refreshToken
                .reuseRefreshTokens(false)
                // 自定义的认证时异常转换
                .exceptionTranslator(customWebResponseExceptionTranslator);
    }


    /**
     * 定义token的存储方式
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        tokenStore.setPrefix(SecurityConst.BALLCAT_OAUTH_PREFIX);
        return tokenStore;
    }


    /**
     * token 增强，追加一些自定义信息
     * @return
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }


}
