package com.hccake.ballcat.common.swagger.annotation;

import com.hccake.ballcat.common.swagger.SwaggerProviderAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 提供者的swagger开启注解
 * @author Hccake
 * @version 1.0
 * @date 2019/11/1 19:43
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ SwaggerProviderAutoConfiguration.class })
public @interface EnableSwagger2Provider {

}
