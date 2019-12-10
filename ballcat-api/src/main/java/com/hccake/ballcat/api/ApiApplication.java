package com.hccake.ballcat.api;

import com.hccake.ballcat.commom.log.access.annotation.EnableAccessLog;
import com.hccake.ballcat.common.job.annotation.EnableXxlJob;
import com.hccake.ballcat.common.swagger.annotation.EnableSwagger2Provider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableXxlJob
@EnableSwagger2Provider
@EnableAccessLog
@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
