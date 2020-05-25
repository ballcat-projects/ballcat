package com.your.packages.admin;

import com.hccake.ballcat.commom.log.access.annotation.EnableAccessLog;
import com.hccake.ballcat.commom.log.operation.annotation.EnableOperationLog;
import com.hccake.ballcat.common.job.annotation.EnableXxlJob;
import com.hccake.ballcat.common.swagger.annotation.EnableSwagger2Aggregator;
import com.hccake.simpleredis.EnableSimpleCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author Hccake
 */
@EnableSimpleCache
@EnableXxlJob
@EnableSwagger2Aggregator
@EnableAccessLog
@EnableOperationLog
@ServletComponentScan("com.hccake.ballcat.admin.oauth.filter")
@SpringBootApplication(scanBasePackages = {"com.hccake.ballcat.admin", "com.your.packages.admin"})
@MapperScan(basePackages = {"com.hccake.ballcat.**.mapper", "com.your.packages.**.mapper"})
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
