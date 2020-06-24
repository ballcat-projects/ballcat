package com.hccake.ballcat.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/5/25 21:01
 */
@MapperScan("com.hccake.ballcat.**.mapper")
@ComponentScan
@ServletComponentScan("com.hccake.ballcat.admin.oauth.filter")
@Configuration
public class UpmsAutoConfiguration {

}
