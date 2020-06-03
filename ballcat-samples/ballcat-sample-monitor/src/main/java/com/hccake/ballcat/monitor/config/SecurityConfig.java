package com.hccake.ballcat.monitor.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.UUID;

/**
 * @author Hccake
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final String adminContextPath;

    public SecurityConfig(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(this.adminContextPath +"/");

        http.authorizeRequests()
                .antMatchers(this.adminContextPath +"/assets/**").permitAll()
                .antMatchers(this.adminContextPath +"/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage(this.adminContextPath +"/login").successHandler(successHandler).and()
                .logout().logoutUrl(this.adminContextPath +"/logout").and()
                .httpBasic().and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers(
                        new AntPathRequestMatcher(this.adminContextPath +"/instances", HttpMethod.POST.toString()),
                        new AntPathRequestMatcher(this.adminContextPath +"/instances/*", HttpMethod.DELETE.toString()),
                        new AntPathRequestMatcher(this.adminContextPath +"/actuator/**")
                )
                .and()
                .rememberMe().key(UUID.randomUUID().toString()).tokenValiditySeconds(1209600);
        // @formatter:on
    }

}

