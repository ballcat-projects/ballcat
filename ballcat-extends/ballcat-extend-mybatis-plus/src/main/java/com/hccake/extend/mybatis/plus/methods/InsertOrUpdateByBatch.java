package com.hccake.extend.mybatis.plus.methods;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.function.Predicate;

/**
 * @author lingting 2020/5/27 11:47
 */
public class InsertOrUpdateByBatch extends BaseInsertBatch {

	protected InsertOrUpdateByBatch() {
		super("insertOrUpdateByBatch");
	}

	protected InsertOrUpdateByBatch(String methodName) {
		super(methodName);
	}

	/**
	 * 字段筛选条件
	 */
	@Setter
	@Accessors(chain = true)
	private Predicate<TableFieldInfo> predicate;

	@Override
	protected String getSql() {
		return "<script>insert into %s %s values %s</script>";
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
				if (!predicate.test(field)) {
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
