package com.hccake.ballcat.admin.i18n.config;

import com.hccake.ballcat.common.model.result.R;
import com.hccake.common.i18n.execute.TranslateExecuteWrapper;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 只针对R的msg进行国际化处理
 *
 * @author Yakir
 */
@RequiredArgsConstructor
@RestControllerAdvice
public class I18nResponseAdvice implements ResponseBodyAdvice<R> {

	private final I18nAdminProperties i18nAdminProperties;

	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
		return isSupport(methodParameter);
	}

	@Override
	public R beforeBodyWrite(R r, MethodParameter methodParameter, MediaType mediaType,
			Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
			ServerHttpResponse serverHttpResponse) {
		String responseMsg = TranslateExecuteWrapper.translateText(i18nAdminProperties.getResponseMsgKey(),
				r.getMessage());
		r.setMessage(responseMsg);
		TranslateExecuteWrapper.translateObject(r.getData());
		return r;
	}

	/**
	 * 仅支持返回值类型为R 与不为错误处理器返回过来的类型
	 * @param methodParameter 方法参数
	 * @return true 支持 false 不支持
	 */
	private boolean isSupport(MethodParameter methodParameter) {
		return methodParameter.getMethod().getAnnotation(ExceptionHandler.class) == null
				&& methodParameter.getParameterType().isAssignableFrom(R.class);
	}

}
