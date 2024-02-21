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

package org.ballcat.mybatisplus.mapper;

import java.util.Collection;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import org.apache.ibatis.annotations.Param;
import org.ballcat.common.model.domain.PageParam;
import org.ballcat.mybatisplus.toolkit.PageUtil;

/**
 * 扩展 BaseMapper 额外提供 {@link InsertBatchSomeColumn} 方法。
 *
 * @link <a href=
 * "https://gitee.com/baomidou/mybatis-plus-samples/tree/master/mybatis-plus-sample-deluxe">自定义
 * BaseMapper 示例</a>
 * @author lingting 2020/5/27 11:39
 */
public interface ExtendMapper<T> extends BaseMapper<T> {

	/**
	 * 根据 PageParam 生成一个 IPage 实例
	 * @param pageParam 分页参数
	 * @param <V> 返回的 Record 对象
	 * @return IPage<V>
	 */
	default <V> IPage<V> prodPage(PageParam pageParam) {
		return PageUtil.prodPage(pageParam);
	}

	/**
	 * 批量插入数据 实现类 {@link InsertBatchSomeColumn}
	 * @param list 数据列表
	 * @return int 改动行
	 */
	int insertBatchSomeColumn(@Param("collection") Collection<T> list);

}
