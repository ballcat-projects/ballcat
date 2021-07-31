package com.hccake.ballcat.autoconfigure.log.annotation;

import com.hccake.ballcat.autoconfigure.log.OperationLogAutoConfiguration;
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
