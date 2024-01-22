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

package org.ballcat.mybatisplus;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.ballcat.mybatisplus.conditions.query.ColumnFunction;
import org.ballcat.mybatisplus.conditions.query.LambdaQueryWrapperX;
import org.ballcat.mybatisplus.toolkit.WrappersX;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 别名的 lambdaAliasQueryX 测试类
 *
 * @author hccake
 */
@Slf4j
class LambdaAliasQueryXTest {

	@Test
	void test() {
		TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Demo.class);

		LambdaQueryWrapperX<Demo> wrapperX = WrappersX.lambdaAliasQueryX(Demo.class);
		// where alias.name = 'xxx'
		wrapperX.like(Demo::getName, "zhangsan");
		// 嵌套 sql
		wrapperX.and(x -> x.like(Demo::getAge, 18));

		Assertions.assertEquals(
				"WHERE (d.name LIKE #{ew.paramNameValuePairs.MPGENVAL1} AND (d.age LIKE #{ew.paramNameValuePairs.MPGENVAL2}))",
				wrapperX.getCustomSqlSegment());
	}

	@Test
	void otherTableAliasTest() {
		TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Demo.class);

		LambdaQueryWrapperX<Demo> wrapperX = WrappersX.lambdaAliasQueryX(Demo.class);
		// where alias.name like 'xxx'
		wrapperX.like(Demo::getName, "zhangsan");

		// 使用其他表进行连查时，需要使用 OtherTableColumnAliasFunction 进行列名构造
		wrapperX.eq(ColumnFunction.create("people.name"), "wangwu");

		log.info("生成的条件语句: {}", wrapperX.getCustomSqlSegment());

		Assertions.assertEquals(
				"WHERE (d.name LIKE #{ew.paramNameValuePairs.MPGENVAL1} AND people.name = #{ew.paramNameValuePairs.MPGENVAL2})",
				wrapperX.getCustomSqlSegment());
	}

}
