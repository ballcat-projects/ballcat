package com.hccake.starter.grpc.annotation;

import com.hccake.starter.grpc.configuration.GrpcServerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author lingting 2023-04-17 09:15
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(GrpcServerConfiguration.class)
public @interface EnableGrpcServer {

}
