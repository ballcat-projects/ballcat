package com.hccake.ballcat.i18n.converter;

import com.hccake.ballcat.i18n.model.dto.I18nDataDTO;
import com.hccake.ballcat.i18n.model.entity.I18nData;
import com.hccake.ballcat.i18n.model.vo.I18nDataExcelVO;
import com.hccake.ballcat.i18n.model.vo.I18nDataPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 国际化信息模型转换器
 *
 * @author hccake 2021-08-06 10:48:25
 */
@Mapper
public interface I18nDataConverter {

	I18nDataConverter INSTANCE = Mappers.getMapper(I18nDataConverter.class);

	/**
	 * PO 转 PageVO
	 * @param i18nData 国际化信息
	 * @return I18nDataPageVO 国际化信息PageVO
	 */
	I18nDataPageVO poToPageVo(I18nData i18nData);

	/**
	 * PO 转 DTI
	 * @param i18nData 国际化信息
	 * @return I18nDataDTO 国际化信息DTO
	 */
	I18nDataDTO poToDto(I18nData i18nData);

	/**
	 * PO 转 ExcelVO
	 * @param i18nData 国际化信息
	 * @return I18nDataExcelVO 国际化信息ExcelVO
	 */
	I18nDataExcelVO poToExcelVo(I18nData i18nData);

	/**
	 * ExcelVO转 PO
	 * @param i18nDataExcelVO 国际化信息ExcelVO
	 * @return I18nData 国际化信息
	 */
	I18nData excelVoToPo(I18nDataExcelVO i18nDataExcelVO);

}
