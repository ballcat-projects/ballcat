package com.hccake.extend.mybatis.plus.methods;

import com.baomidou.mybatisplus.core.metadata.TableInfo;

/**
 * @author lingting 2020/5/27 11:47
 */
public class InsertOrUpdateFieldByBatch extends BaseInsertBatch {

	private static final String SQL = "<script>insert into %s %s values %s</script>";

	protected InsertOrUpdateFieldByBatch() {
		super("insertOrUpdateFieldByBatch");
	}

	protected InsertOrUpdateFieldByBatch(String methodName) {
		super(methodName);
	}

	@Override
	protected String getSql() {
		return SQL;
	}

	@Override
	protected String prepareValuesSqlForMysqlBatch(TableInfo tableInfo) {
		StringBuilder sql = super.prepareValuesBuildSqlForMysqlBatch(tableInfo);
		sql.append(" ON DUPLICATE KEY UPDATE ")
				// 如果模式为 不忽略设置的字段
				.append("<if test=\"!columns.ignore\">")
				.append("<foreach collection=\"columns.list\" item=\"item\" index=\"index\" separator=\",\" >")
				.append("${item.name}=${item.val}").append("</foreach>").append("</if>");

		// 如果模式为 忽略设置的字段
		sql.append("<if test=\"columns.ignore\">")
				.append("<foreach collection=\"columns.back\" item=\"item\" index=\"index\" separator=\",\" >")
				.append("${item}=VALUES(${item})").append("</foreach>").append("</if>");
		return sql.toString();
	}

}
