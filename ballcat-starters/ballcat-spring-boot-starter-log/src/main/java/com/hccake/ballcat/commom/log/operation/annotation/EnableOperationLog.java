package com.hccake.ballcat.commom.log.operation.annotation;

import com.hccake.ballcat.commom.log.operation.OperationLogAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 18:11
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ OperationLogAutoConfiguration.class })
public @interface EnableOperationLog {

}
