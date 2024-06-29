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

package org.ballcat.web.pageable;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.model.domain.PageParam;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author lengleng
 * @author hccake 2019-06-24
 * <p>
 * 解决Mybatis Plus Order By SQL注入问题
 */
@Slf4j
public class DefaultPageParamArgumentResolver extends PageParamArgumentResolverSupport
		implements PageParamArgumentResolver {

	public DefaultPageParamArgumentResolver(PageableRequestOptions defaultPageableRequestOptions) {
		this.setDefaultPageableRequestOptions(defaultPageableRequestOptions);
	}

	/**
	 * 判断Controller是否包含page 参数
	 * @param parameter 参数
	 * @return 是否过滤
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return PageParam.class.isAssignableFrom(parameter.getParameterType());
	}

	/**
	 * page 只支持查询 GET .如需解析POST获取请求报文体处理.
	 * @param parameter 入参集合
	 * @param mavContainer model 和 view
	 * @param webRequest web相关
	 * @param binderFactory 入参解析
	 * @return 检查后新的page对象
	 * @throws Exception ex
	 */
	@NonNull
	@Override
	public PageParam resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		if (request == null) {
			return new PageParam();
		}

		Pageable pageableAnnotation = parameter.getParameterAnnotation(Pageable.class);
		PageableRequestOptions pageableRequestOptions = pageableAnnotation == null
				? this.getDefaultPageableRequestOptions() : getPageableRequestOptions(pageableAnnotation);

		PageParam pageParam = getPageParam(parameter, request, pageableRequestOptions);
		paramValidate(parameter, mavContainer, webRequest, binderFactory, pageParam, pageableRequestOptions);
		return pageParam;
	}

	private static PageableRequestOptions getPageableRequestOptions(Pageable pageableAnnotation) {
		return new PageableRequestOptions().setMaxPageSize(pageableAnnotation.maxPageSize())
			.setPageParameterName(pageableAnnotation.pageParameterName())
			.setSizeParameterName(pageableAnnotation.sizeParameterName())
			.setSortParameterName(pageableAnnotation.sortParameterName());
	}

}
