package com.hccake.ballcat.admin;

import com.hccake.ballcat.commom.log.access.annotation.EnableAccessLog;
import com.hccake.ballcat.common.swagger.annotation.EnableSwagger2Aggregator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author Hccake
 */
/*@EnableXxlJob*/
@EnableSwagger2Aggregator
@EnableAccessLog
@ServletComponentScan("com.hccake.ballcat.admin.oauth.filter")
@SpringBootApplication
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
