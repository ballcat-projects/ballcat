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

package org.ballcat.fieldcrypt.mybatis.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.annotations.Param;
import org.ballcat.fieldcrypt.annotation.DecryptResult;
import org.ballcat.fieldcrypt.annotation.Encrypted;

/**
 * 基于 MappedStatement.id 解析缓存方法参数元数据。 解析策略：namespace = Mapper 接口 FQN；methodName = 最终
 * segment；按名称匹配； 若存在重载，挑选参数个数匹配且至少出现一个 @DataShield 的方法；否则取第一个名称匹配方法。
 *
 * @author Hccake
 * @since 2.0.0
 */
public class MybatisMethodMetadataCache {

	private final Map<String, MybatisMethodMetadata> cache = new ConcurrentHashMap<>();

	public MybatisMethodMetadata getOrParse(String msId) {
		return this.cache.computeIfAbsent(msId, this::parseInternal);
	}

	private MybatisMethodMetadata parseInternal(String msId) {
		int pos = msId.lastIndexOf('.');
		if (pos < 0) {
			return new MybatisMethodMetadata(new MybatisParameterMetadata[0]);
		}
		String className = msId.substring(0, pos);
		String methodName = msId.substring(pos + 1);
		try {
			Class<?> mapper = Class.forName(className);
			Method[] methods = mapper.getMethods();
			Method candidate = null;
			Method annotatedPreferred = null;
			for (Method m : methods) {
				if (!m.getName().equals(methodName)) {
					continue;
				}
				if (candidate == null) {
					candidate = m;
				}
				if (hasAnyDataShieldParam(m)) {
					annotatedPreferred = m; // 若多个，后面继续覆盖，最终取最后一个（通常无影响）
				}
			}
			Method target = annotatedPreferred != null ? annotatedPreferred : candidate;
			if (target == null) {
				return new MybatisMethodMetadata(new MybatisParameterMetadata[0]);
			}
			Parameter[] params = target.getParameters();
			MybatisParameterMetadata[] metas = new MybatisParameterMetadata[params.length];
			for (int i = 0; i < params.length; i++) {
				Parameter p = params[i];
				Encrypted ds = findDataShield(p.getAnnotations());
				Param paramAnno = p.getAnnotation(Param.class);
				String paramName = paramAnno != null ? paramAnno.value() : null;
				if (ds != null) {
					metas[i] = new MybatisParameterMetadata(true, ds.algo(), ds.params(), ds.mapKeys(), p.getType(),
							paramName);
				}
				else {
					metas[i] = new MybatisParameterMetadata(false, "", "", new String[0], p.getType(), paramName);
				}
			}
			// 解析方法级结果注解
			DecryptResult resultAnno = target.getAnnotation(DecryptResult.class);
			MybatisResultMetadata resultMeta = resultAnno == null ? MybatisResultMetadata.ABSENT
					: new MybatisResultMetadata(true, resultAnno.algo(), resultAnno.params());
			return new MybatisMethodMetadata(metas, resultMeta);
		}
		catch (ClassNotFoundException e) {
			return new MybatisMethodMetadata(new MybatisParameterMetadata[0]);
		}
	}

	private boolean hasAnyDataShieldParam(Method m) {
		for (Parameter p : m.getParameters()) {
			if (findDataShield(p.getAnnotations()) != null) {
				return true;
			}
		}
		return false;
	}

	private Encrypted findDataShield(Annotation[] annotations) {
		for (Annotation a : annotations) {
			if (a.annotationType() == Encrypted.class) {
				return (Encrypted) a;
			}
		}
		return null;
	}

}
