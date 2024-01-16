package org.ballcat.easyexcel.util;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ConverterKeyBuild.ConverterKey;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Objects;

/**
 * @author lingting 2024-01-11 15:25
 */
@UtilityClass
@SuppressWarnings("java:S1452")
public class EasyExcelUtils {

	public static Converter<?> find(ConverterKey key, Map<ConverterKey, Converter<?>> map) {
		// 先直接获取
		Converter<?> converter = map.get(key);
		if (converter != null) {
			return converter;
		}

		// 通过继承关系获取
		for (Map.Entry<ConverterKey, Converter<?>> entry : map.entrySet()) {
			ConverterKey converterKey = entry.getKey();
			Converter<?> value = entry.getValue();

			// 行数据类型不一致, 跳过
			if (Objects.equals(converterKey.getCellDataTypeEnum(), key.getCellDataTypeEnum())) {
				Class<?> converterClz = converterKey.getClazz();

				// Java数据类型匹配
				if (converterClz.isAssignableFrom(key.getClazz())) {
					return value;
				}
			}
		}
		return null;
	}

}
