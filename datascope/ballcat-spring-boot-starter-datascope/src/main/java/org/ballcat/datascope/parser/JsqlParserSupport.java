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

package org.ballcat.datascope.parser;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

/**
 * <a href="https://github.com/JSQLParser/JSqlParser">JSqlParser</a>
 *
 * @author miemie hccake
 * @since 2020-06-22
 */
@Slf4j
public abstract class JsqlParserSupport {

	public String parserSingle(String sql, Object obj) {
		try {
			Statement statement = CCJSqlParserUtil.parse(sql);
			return processParser(statement, 0, sql, obj);
		}
		catch (JSQLParserException e) {
			throw new RuntimeException(String.format("Failed to process, Error SQL: %s", sql), e);
		}
	}

	public String parserMulti(String sql, Object obj) {
		try {
			// fixed github pull/295
			StringBuilder sb = new StringBuilder();
			Statements statements = CCJSqlParserUtil.parseStatements(sql);
			int i = 0;
			for (Statement statement : statements.getStatements()) {
				if (i > 0) {
					sb.append(";");
				}
				sb.append(processParser(statement, i, sql, obj));
				i++;
			}
			return sb.toString();
		}
		catch (JSQLParserException e) {
			throw new RuntimeException(String.format("Failed to process, Error SQL: %s", sql), e);
		}
	}

	/**
	 * 执行 SQL 解析
	 * @param statement JsqlParser Statement
	 * @return sql
	 */
	protected String processParser(Statement statement, int index, String sql, Object obj) {
		if (log.isDebugEnabled()) {
			log.debug("SQL to parse, SQL: " + sql);
		}
		if (statement instanceof Insert) {
			this.processInsert((Insert) statement, index, sql, obj);
		}
		else if (statement instanceof Select) {
			this.processSelect((Select) statement, index, sql, obj);
		}
		else if (statement instanceof Update) {
			this.processUpdate((Update) statement, index, sql, obj);
		}
		else if (statement instanceof Delete) {
			this.processDelete((Delete) statement, index, sql, obj);
		}
		sql = statement.toString();
		if (log.isDebugEnabled()) {
			log.debug("parse the finished SQL: " + sql);
		}
		return sql;
	}

	/**
	 * 新增
	 */
	protected void processInsert(Insert insert, int index, String sql, Object obj) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 删除
	 */
	protected void processDelete(Delete delete, int index, String sql, Object obj) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 更新
	 */
	protected void processUpdate(Update update, int index, String sql, Object obj) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 查询
	 */
	protected void processSelect(Select select, int index, String sql, Object obj) {
		throw new UnsupportedOperationException();
	}

}
