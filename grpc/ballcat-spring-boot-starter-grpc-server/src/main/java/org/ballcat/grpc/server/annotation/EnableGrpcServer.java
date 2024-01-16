package org.ballcat.grpc.server.annotation;

import org.ballcat.grpc.server.configuration.GrpcServerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
