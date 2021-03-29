package com.hccake.ballcat.common.desensitize;

import cn.hutool.core.lang.Assert;
import com.hccake.ballcat.common.desensitize.enums.RegexDesensitizationTypeEnum;
import com.hccake.ballcat.common.desensitize.enums.SlideDesensitizationTypeEnum;
import com.hccake.ballcat.common.desensitize.functions.DesensitizeFunction;
import com.hccake.ballcat.common.desensitize.handler.RegexDesensitizationHandler;
import com.hccake.ballcat.common.desensitize.handler.SimpleDesensitizationHandler;
import com.hccake.ballcat.common.desensitize.handler.SlideDesensitizationHandler;
import com.hccake.ballcat.common.desensitize.json.annotation.JsonRegexDesensitize;
import com.hccake.ballcat.common.desensitize.json.annotation.JsonSimpleDesensitize;
import com.hccake.ballcat.common.desensitize.json.annotation.JsonSlideDesensitize;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 注解处理方法 脱敏注解->处理逻辑
 *
 * @author Yakir
 */
public class AnnotationHandlerHolder {

	private AnnotationHandlerHolder() {

	}

	/**
	 * 注解类型 处理函数映射
	 */
	private static final Map<Class<? extends Annotation>, DesensitizeFunction> ANNOTATION_HANDLER = new HashMap<>();

	static {

		ANNOTATION_HANDLER.put(JsonSimpleDesensitize.class, (annotation, value) -> {
			// Simple 类型处理
			JsonSimpleDesensitize an = (JsonSimpleDesensitize) annotation;
			Class<? extends SimpleDesensitizationHandler> handlerClass = an.handler();
			SimpleDesensitizationHandler desensitizationHandler = DesensitizationHandlerHolder
					.getSimpleHandler(handlerClass);
			Assert.notNull(desensitizationHandler, "SimpleDesensitizationHandler can not be Null");
			return desensitizationHandler.handle(value);
		});

		ANNOTATION_HANDLER.put(JsonRegexDesensitize.class, (annotation, value) -> {
			// 正则类型脱敏处理
			JsonRegexDesensitize an = (JsonRegexDesensitize) annotation;
			RegexDesensitizationTypeEnum type = an.type();
			RegexDesensitizationHandler regexDesensitizationHandler = DesensitizationHandlerHolder
					.getRegexDesensitizationHandler();
			return RegexDesensitizationTypeEnum.CUSTOM.equals(type)
					? regexDesensitizationHandler.handle(value, an.regex(), an.replacement())
					: regexDesensitizationHandler.handle(value, type);
		});
		ANNOTATION_HANDLER.put(JsonSlideDesensitize.class, (annotation, value) -> {
			// 滑动类型脱敏处理
			JsonSlideDesensitize an = (JsonSlideDesensitize) annotation;
			SlideDesensitizationTypeEnum type = an.type();
			SlideDesensitizationHandler slideDesensitizationHandler = DesensitizationHandlerHolder
					.getSlideDesensitizationHandler();
			return SlideDesensitizationTypeEnum.CUSTOM.equals(type) ? slideDesensitizationHandler.handle(value,
					an.leftPlainTextLen(), an.rightPlainTextLen(), an.maskString())
					: slideDesensitizationHandler.handle(value, type);
		});
	}

	/**
	 * 得到注解类型处理函数
	 * @param annotationType {@code annotationType} 注解类型
	 * @return {@link DesensitizeFunction}脱敏处理函数|null
	 */
	public static DesensitizeFunction getHandleFunction(Class<? extends Annotation> annotationType) {
		return ANNOTATION_HANDLER.get(annotationType);
	}

	/**
	 * 添加注解处理函数
	 * @param annotationType {@code annotationType}指定注解类型
	 * @param desensitizeFunction {@link DesensitizeFunction}指定脱敏处理函数
	 * @return 上一个key 对应的脱敏处理函数 | null
	 */
	public static DesensitizeFunction addHandleFunction(Class<? extends Annotation> annotationType,
			DesensitizeFunction desensitizeFunction) {
		Assert.notNull(annotationType, "annotation cannot be null");
		Assert.notNull(desensitizeFunction, "desensitization function cannot be null");
		// 加入注解处理映射
		return ANNOTATION_HANDLER.put(annotationType, desensitizeFunction);
	}

	/**
	 * 得到当前支持的注解处理类
	 * @return {@code 注解处理类列表}
	 */
	public static Set<Class<? extends Annotation>> getAnnotationClasses() {
		return ANNOTATION_HANDLER.keySet();
	}

}
