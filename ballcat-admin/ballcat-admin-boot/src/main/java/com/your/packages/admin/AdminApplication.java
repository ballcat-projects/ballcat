package com.your.packages.admin;

import com.hccake.ballcat.commom.log.access.annotation.EnableAccessLog;
import com.hccake.ballcat.commom.log.operation.annotation.EnableOperationLog;
import com.hccake.ballcat.common.job.annotation.EnableXxlJob;
import com.hccake.ballcat.common.swagger.annotation.EnableSwagger2Aggregator;
import com.hccake.simpleredis.EnableSimpleCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Hccake
 */
@EnableSimpleCache
@EnableXxlJob
@EnableSwagger2Aggregator
@EnableAccessLog
@EnableOperationLog
@MapperScan("com.your.packages.**.mapper")
@SpringBootApplication
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
