package com.hccake.ballcat.autoconfigure.web.pageable;

import com.hccake.ballcat.common.model.domain.PageParam;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 分页参数解析器
 *
 * @author hccake
 */
public interface PageParamArgumentResolver extends HandlerMethodArgumentResolver {

	/**
	 * Resolves a {@link PageParam} method parameter into an argument value from a given
	 * request.
	 * @param parameter the method parameter to resolve. This parameter must have
	 * previously been passed to {@link #supportsParameter} which must have returned
	 * {@code true}.
	 * @param mavContainer the ModelAndViewContainer for the current request
	 * @param webRequest the current request
	 * @param binderFactory a factory for creating {@link WebDataBinder} instances
	 * @return the resolved argument value.
	 * @throws Exception ex
	 */
	@NonNull
	@Override
	PageParam resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception;

}
