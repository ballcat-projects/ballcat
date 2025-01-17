/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.log.operation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ballcat.log.operation.handler.OperationLogHandler;
import org.ballcat.log.operation.provider.DefaultTraceIdProvider;
import org.ballcat.log.operation.provider.HttpInfoProvider;

/**
 * 操作日志注解，用于标注需要记录操作日志的方法。
 *
 * @author Hccake 2019/10/15 18:09
 * @since 2.0.0
 */
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

	/**
	 * 业务类型，如订单、用户、商品等。
	 */
	String bizType();

	/**
	 * 业务子类型，如订单支付、订单退款等。
	 */
	String subType() default "";

	/**
	 * 业务标识，用于关联业务数据，如订单号、用户ID等，支持使用 SpEL 表达式。
	 */
	String bizNo() default "";

	/**
	 * 操作成功时的消息模板，支持使用 SpEL 表达式。
	 */
	String successMessage() default "操作成功";

	/**
	 * 操作失败时的消息模板，支持使用 SpEL 表达式。
	 */
	String failureMessage() default "操作失败";

	/**
	 * 操作执行出现错误时的消息模板，支持使用 SpEL 表达式。
	 */
	String errorMessage() default "操作出现错误";

	/**
	 * <p>
	 * 操作人信息，支持使用 SpEL 表达式， 用于标识触发该操作的用户或系统实体。
	 * </p>
	 */
	String operator() default "#{@defaultOperatorProvider.get()}";

	/**
	 * <p>
	 * 追踪ID，支持使用 SpEL 表达式。
	 * </p>
	 * 默认通过 defaultTraceIdProvider 获取当前操作人信息。
	 *
	 * @see DefaultTraceIdProvider
	 */
	String traceId() default "#{@defaultTraceIdProvider.get()}";

	/**
	 * HttpInfoProvider 类型，用于提供 HTTP 请求信息。
	 */
	Class<? extends HttpInfoProvider> httpInfoProvider() default HttpInfoProvider.class;

	/**
	 * OperationLogHandler 类型，处理操作日志进行输出或者持久化等操作。
	 */
	Class<? extends OperationLogHandler> logHandler() default OperationLogHandler.class;

	/**
	 * 日志的额外信息，支持使用 SpEL 表达式.
	 */
	String extra() default "";

	/**
	 * 是否记录日志的 SpEL 条件表达式，解析值必须是 boolean 类型。
	 */
	String condition() default "";

	/**
	 * 判断操作是否成功的 SpEL 表达式，默认为空，表示不抛异常即为成功，解析值必须是 boolean 类型。
	 */
	String successCondition() default "";

	/**
	 * 是否记录方法的完整入参，默认为 true。
	 */
	boolean isRecordArgs() default true;

	/**
	 * 是否记录方法的完整返回值，默认为 true。
	 */
	boolean isRecordResult() default true;

}
