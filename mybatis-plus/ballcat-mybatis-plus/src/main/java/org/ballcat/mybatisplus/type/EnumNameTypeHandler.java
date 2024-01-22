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

package org.ballcat.mybatisplus.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.baomidou.mybatisplus.annotation.IEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

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
		else {
			Object val = getValByEnum(parameter);
			if (jdbcType == null) {
				ps.setString(i, val == null ? null : val.toString());
			}
			else {
				ps.setObject(i, val, jdbcType.TYPE_CODE);
			}
		}
	}

	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return getEnumByName(rs.getString(columnName));
	}

	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return getEnumByName(rs.getString(columnIndex));
	}

	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return getEnumByName(cs.getString(columnIndex));
	}

	boolean isIEnum(E e) {
		return IEnum.class.isAssignableFrom(e.getClass());
	}

	Object getValByEnum(E e) {
		// IEnum
		if (isIEnum(e)) {
			return ((IEnum<?>) e).getValue();
		}
		return e.name();
	}

	/**
	 * 根据枚举 name() 获取枚举
	 * @author lingting 2021-06-07 13:50
	 */
	E getEnumByName(String val) {
		for (E e : this.type.getEnumConstants()) {
			Object ev = getValByEnum(e);
			if (ev == null) {
				if (val == null) {
					return e;
				}
				continue;
			}

			if (val.equals(ev.toString())) {
				return e;
			}
		}
		return null;
	}

}
