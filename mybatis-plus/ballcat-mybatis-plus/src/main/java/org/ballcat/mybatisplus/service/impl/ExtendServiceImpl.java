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

package org.ballcat.mybatisplus.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import org.ballcat.mybatisplus.mapper.ExtendMapper;
import org.ballcat.mybatisplus.service.ExtendService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 继承 BaseServiceImpl 接口，额外提供了 saveBatchSomeColumn 方法。
 * <p>
 * 注意：该类引用的是 ExtendMapper，用户需按 MP 文档注入 {@link InsertBatchSomeColumn} 方法
 *
 * @link <a href="https://baomidou.com/pages/42ea4a/">SQL注入器</a>
 * @author lingting 2020/7/21 10:00
 */
public class ExtendServiceImpl<M extends ExtendMapper<T>, T> extends BaseServiceImpl<M, T> implements ExtendService<T> {

	/**
	 * 批量插入数据
	 * @param list 数据列表
	 * @param batchSize 批次插入数据量
	 * @return int 改动行
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveBatchSomeColumn(Collection<T> list, int batchSize) {
		if (CollectionUtils.isEmpty(list)) {
			return false;
		}
		final int batch = Math.min(list.size(), batchSize);
		List<T> subList = new ArrayList<>(batch);
		for (T t : list) {
			if (subList.size() >= batch) {
				this.baseMapper.insertBatchSomeColumn(subList);
				subList = new ArrayList<>(batch);
			}
			subList.add(t);
		}
		if (CollectionUtils.isEmpty(subList)) {
			this.baseMapper.insertBatchSomeColumn(subList);
		}
		return true;
	}

}
