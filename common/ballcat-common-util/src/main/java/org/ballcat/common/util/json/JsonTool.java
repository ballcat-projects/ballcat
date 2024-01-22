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

package org.ballcat.common.util.json;

import java.lang.reflect.Type;

/**
 * json 相关 util 类需实现本类。
 *
 * @author lingting 2021/2/25 20:43
 */
public interface JsonTool {

	/**
	 * obj -> jsonStr
	 * @param obj obj
	 * @return java.lang.String
	 */
	String toJson(Object obj);

	/**
	 * jsonStr -> obj
	 * @param json json str
	 * @param r obj.class
	 * @return T
	 */
	<T> T toObj(String json, Class<T> r);

	/**
	 * jsonStr -> obj
	 * @param json json str
	 * @param t (obj.class)type
	 * @return T
	 */
	<T> T toObj(String json, Type t);

	/**
	 *
	 * jsonStr -> obj
	 * @param json json str
	 * @param t TypeReference
	 * @return T
	 */
	<T> T toObj(String json, TypeReference<T> t);

}
