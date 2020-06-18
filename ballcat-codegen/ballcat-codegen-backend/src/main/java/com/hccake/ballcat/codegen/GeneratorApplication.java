package com.hccake.ballcat.codegen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/12 16:21
 */
@MapperScan("com.hccake.ballcat.codegen.mapper")
@SpringBootApplication
public class GeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeneratorApplication.class);
    }
}
