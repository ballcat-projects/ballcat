package com.hccake.extend.mybatis.plus.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 扩展 mybatis 的 SelectPage，使其返回 records 可以直接返回 VO
 *
 * @author Hccake 2021/1/18
 * @version 1.0
 */
public class SelectByPage extends AbstractMethod {

	/**
	 * 注入方法的唯一ID，对应mapper的方法名
	 */
	private static final String METHOD_ID = "selectByPage";

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		String sqlTemplate = SqlMethod.SELECT_PAGE.getSql();
		String sql = String.format(sqlTemplate, sqlFirst(), sqlSelectColumns(tableInfo, true), tableInfo.getTableName(),
				sqlWhereEntityWrapper(true, tableInfo), sqlComment());
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
		return this.addSelectMappedStatementForTable(mapperClass, METHOD_ID, sqlSource, tableInfo);
	}

}
