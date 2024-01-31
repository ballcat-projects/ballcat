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

package org.ballcat.mybatisplus.conditions.query;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.Getter;
import lombok.Setter;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.ballcat.mybatisplus.alias.TableAlias;
import org.ballcat.mybatisplus.toolkit.WrappersX;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lingting 2024-01-30 16:12
 */
class LambdaQueryWrapperXTest {

	@BeforeEach
	void before() {
		TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Entity.class);
	}

	@Test
	void test() {
		assertContains("AND  a.string='addSql'", wrapper().eq(Entity::getString, "eq1").addSql("a.string='addSql'"));
		assertContains(" a.string='addSql'", wrapper().addSql("a.string='addSql'"));

		Column column = new Column("a.string");
		assertContains(" (a.string = 'ex1')", wrapper().addExpression(new EqualsTo(column, new StringValue("ex1"))));

		assertContains("AND  (a.string = 'ex1' AND a.string = 'ex2')",
				wrapper().eq(Entity::getString, "eq1")
					.addExpression(new AndExpression(new EqualsTo(column, new StringValue("ex1")),
							new EqualsTo(column, new StringValue("ex2")))));

		assertContains(
				"JSON_CONTAINS(a.list,JSON_ARRAY(#{ew.paramNameValuePairs.MPGENVAL1},#{ew.paramNameValuePairs.MPGENVAL2}))",
				wrapper().jsonContainsIfPresent(Entity::getList, Arrays.asList("1", "2")));
		assertContains(
				" JSON_CONTAINS(a.list,JSON_ARRAY(#{ew.paramNameValuePairs.MPGENVAL1},#{ew.paramNameValuePairs.MPGENVAL2})) AND",
				wrapper().jsonContains(Entity::getList, "1", "2").eq(Entity::getString, "eq1"));
	}

	void assertContains(String contains, LambdaQueryWrapperX<Entity> wrapperX) {
		String string = wrapperX.getCustomSqlSegment();
		System.out.println(string);
		assertTrue(string.contains(contains));
	}

	LambdaQueryWrapperX<Entity> wrapper() {
		return WrappersX.lambdaAliasQueryX(Entity.class);
	}

	@Getter
	@Setter
	@TableAlias("a")
	static class Entity {

		private String string;

		private List<String> list;

	}

}
