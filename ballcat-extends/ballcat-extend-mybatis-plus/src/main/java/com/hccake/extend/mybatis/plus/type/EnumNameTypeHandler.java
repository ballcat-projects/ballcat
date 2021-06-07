package com.hccake.extend.mybatis.plus.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

/**
 * 普通枚举类型处理. 根据 name() 返回值判断枚举值
 *
 * @author lingting 2021/6/7 13:49
 */
public class EnumNameTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

	private final Class<E> type;

	public EnumNameTypeHandler(Class<E> type) {
		if (type == null) {
			throw new IllegalArgumentException("Type argument cannot be null");
		}
		this.type = type;
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		if (parameter == null) {
			ps.setString(i, "");
		}
		else if (jdbcType == null) {
			ps.setString(i, parameter.name());
		}
		else {
			ps.setObject(i, parameter.name(), jdbcType.TYPE_CODE);
		}
	}

	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String s = rs.getString(columnName);
		return StringUtils.hasText(s) ? getEnumByName(s) : null;
	}

	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String s = rs.getString(columnIndex);
		return StringUtils.hasText(s) ? getEnumByName(s) : null;
	}

	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String s = cs.getString(columnIndex);
		return StringUtils.hasText(s) ? getEnumByName(s) : null;
	}

	/**
	 * 根据枚举 name() 获取枚举
	 * @author lingting 2021-06-07 13:50
	 */
	E getEnumByName(String name) {
		for (E e : type.getEnumConstants()) {
			if (e.name().equals(name)) {
				return e;
			}
		}
		return null;
	}

}
