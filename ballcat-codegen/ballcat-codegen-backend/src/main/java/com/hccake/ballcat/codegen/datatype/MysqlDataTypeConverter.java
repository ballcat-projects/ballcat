package com.hccake.ballcat.codegen.datatype;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/22 17:53
 */
public class MysqlDataTypeConverter {
	public static final Map<String, String> MAPPING = new HashMap<>();
	static {
		MAPPING.put("tinyint", "Integer");
		MAPPING.put("smallint", "Integer");
		MAPPING.put("mediumint", "Integer");
		MAPPING.put("int", "Integer");
		MAPPING.put("integer", "Integer");
		MAPPING.put("bigint", "Long");
		MAPPING.put("float", "Float");
		MAPPING.put("double", "Double");
		MAPPING.put("decimal", "BigDecimal");
		MAPPING.put("bit", "Boolean");

		MAPPING.put("char", "String");
		MAPPING.put("varchar", "String");
		MAPPING.put("tinytext", "String");
		MAPPING.put("text", "String");
		MAPPING.put("mediumtext", "String");
		MAPPING.put("longtext", "String");

		MAPPING.put("date", "LocalDateTime");
		MAPPING.put("datetime", "LocalDateTime");
		MAPPING.put("timestamp", "LocalDateTime");
	}

}
