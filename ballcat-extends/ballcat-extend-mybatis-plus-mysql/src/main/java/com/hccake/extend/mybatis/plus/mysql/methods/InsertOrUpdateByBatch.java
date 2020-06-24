package com.hccake.extend.mybatis.plus.mysql.methods;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.hccake.extend.mybatis.plus.config.StaticConfig;

/**
 * @author lingting 2020/5/27 11:47
 */
public class InsertOrUpdateByBatch extends BaseInsertBatch {

	@Override
	protected String getSql() {
		return "<script>insert into %s %s values %s</script>";
	}

	@Override
	protected String getId() {
		return "insertOrUpdateByBatch";
	}

	@Override
	protected String prepareValuesSqlForMysqlBatch(TableInfo tableInfo) {
		StringBuilder sql = super.prepareValuesBuildSqlForMysqlBatch(tableInfo);
		sql.append(" ON DUPLICATE KEY UPDATE ");
		StringBuilder ignore = new StringBuilder();

		tableInfo.getFieldList().forEach(field -> {
			// 默认忽略逻辑删除字段
			if (!field.isLogicDelete()) {
				// 默认忽略字段
				if (!StaticConfig.UPDATE_IGNORE_FIELDS.contains(field.getProperty())) {
					sql.append(field.getColumn()).append("=").append("VALUES(").append(field.getColumn()).append("),");
				}
				else {
					ignore.append(",").append(field.getColumn()).append("=").append("VALUES(").append(field.getColumn())
							.append(")");
				}
			}
		});

		// 删除最后一个多余的逗号
		sql.delete(sql.length() - 1, sql.length());

		// 配置不忽略全局配置字段时的sql部分
		sql.append("<if test=\"!ignore\">").append(ignore).append("</if>");
		return sql.toString();
	}

}
