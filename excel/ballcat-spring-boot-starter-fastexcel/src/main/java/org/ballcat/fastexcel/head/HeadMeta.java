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

package org.ballcat.fastexcel.head;

import java.util.List;
import java.util.Set;

import lombok.Data;

/**
 * HeadMetaInfo
 *
 * @author Yakir 2021/4/26 10:58
 *
 */
@Data
public class HeadMeta {

	/**
	 * <p>
	 * 自定义头部信息
	 * </p>
	 * 实现类根据数据的class信息，定制Excel头<br/>
	 * 具体方法使用参考：<a href=
	 * "https://idev.cn/fastexcel/zh-CN/docs/write/write_hard#%E5%8A%A8%E6%80%81%E5%A4%B4%E5%86%99%E5%85%A5">动态头写入</a>
	 */
	private List<List<String>> head;

	/**
	 * 忽略头对应字段名称
	 */
	private Set<String> ignoreHeadFields;

}
