package com.hccake.ballcat.admin.modules.lov.typehandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.ballcat.admin.modules.lov.model.entity.LovSelectOptions;
import com.hccake.ballcat.common.conf.util.SpringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lingting 2020-08-10 15:44
 */
public class ListLovSelectOptionTypeHandler implements TypeHandler<List<LovSelectOptions>> {

	public ObjectMapper om = null;

	public ObjectMapper getOm() {
		if (om == null) {
			om = SpringUtils.getBean(ObjectMapper.class);
		}
		return om;
	}

	@Override
	public void setParameter(PreparedStatement ps, int i, List<LovSelectOptions> parameter, JdbcType jdbcType)
			throws SQLException {
		try {
			ps.setString(i, getOm().writeValueAsString(parameter));
		}
		catch (Exception e) {
			ps.setString(i, "[]");
		}
	}

	@Override
	public List<LovSelectOptions> getResult(ResultSet rs, String columnName) throws SQLException {
		return toVal(rs.getString(columnName));
	}

	@Override
	public List<LovSelectOptions> getResult(ResultSet rs, int columnIndex) throws SQLException {
		return toVal(rs.getString(columnIndex));
	}

	@Override
	public List<LovSelectOptions> getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return toVal(cs.getString(columnIndex));
	}

	List<LovSelectOptions> toVal(String string) {
		try {
			return getOm().readValue(string, new TypeReference<List<LovSelectOptions>>() {
			});
		}
		catch (Exception e) {
			return new ArrayList<>();
		}
	}

}
