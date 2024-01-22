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

package org.ballcat.easyexcel.converters;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * LocalDate and string converter
 *
 * @author L.cm
 */
public enum LocalDateStringConverter implements Converter<LocalDate> {

	/**
	 * 实例
	 */
	INSTANCE;

	@Override
	public Class<?> supportJavaTypeKey() {
		return LocalDate.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public LocalDate convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) throws ParseException {
		if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
			return LocalDate.parse(cellData.getStringValue());
		}
		else {
			DateTimeFormatter formatter = DateTimeFormatter
				.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat());
			return LocalDate.parse(cellData.getStringValue(), formatter);
		}
	}

	@Override
	public WriteCellData<String> convertToExcelData(LocalDate value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		DateTimeFormatter formatter;
		if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
			formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		}
		else {
			formatter = DateTimeFormatter.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat());
		}
		return new WriteCellData<>(value.format(formatter));
	}

}
