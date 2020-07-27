package com.hccake.extend.mybatis.plus.mysql.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 所有插入自定义方法的父类
 *
 * @author lingting 2020/5/27 15:14
 */
public abstract class BaseInsertBatch extends AbstractMethod {

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, String.format(getSql(),
				tableInfo.getTableName(), prepareFieldSql(tableInfo), prepareValuesSqlForMysqlBatch(tableInfo)),
				modelClass);
		return this.addInsertMappedStatement(mapperClass, modelClass, getId(), sqlSource, new NoKeyGenerator(), null,
				null);
	}

	protected String prepareFieldSql(TableInfo tableInfo) {
		StringBuilder fieldSql = new StringBuilder();
		fieldSql.append(tableInfo.getKeyColumn()).append(",");
		tableInfo.getFieldList().forEach(x -> fieldSql.append(x.getColumn()).append(","));
		fieldSql.delete(fieldSql.length() - 1, fieldSql.length());
		fieldSql.insert(0, "(");
		fieldSql.append(")");
		return fieldSql.toString();
	}

	/**
	 * 获取注册的脚本
	 * @return java.lang.String
	 * @author lingting 2020-06-09 20:38:54
	 */
	abstract protected String getSql();

	/**
	 * 获取脚本id 即 方法名
	 * @return java.lang.String
	 * @author lingting 2020-06-09 20:39:30
	 */
	abstract protected String getId();

	protected String prepareValuesSqlForMysqlBatch(TableInfo tableInfo) {
		return prepareValuesBuildSqlForMysqlBatch(tableInfo).toString();
	}

	protected StringBuilder prepareValuesBuildSqlForMysqlBatch(TableInfo tableInfo) {
		final StringBuilder valueSql = new StringBuilder();
		valueSql.append(
				"<foreach collection=\"collection\" item=\"item\" index=\"index\" open=\"(\" separator=\"),(\" close=\")\">");
		valueSql.append("#{item.").append(tableInfo.getKeyProperty()).append("},");
		tableInfo.getFieldList().forEach(x -> valueSql.append("#{item.").append(x.getProperty()).append("},"));
		valueSql.delete(valueSql.length() - 1, valueSql.length());
		valueSql.append("</foreach>");
		return valueSql;
	}

}
