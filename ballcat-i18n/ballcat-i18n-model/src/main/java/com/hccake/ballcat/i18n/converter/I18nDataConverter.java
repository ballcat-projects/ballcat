package com.hccake.ballcat.i18n.converter;

import com.hccake.ballcat.i18n.model.entity.I18nData;
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

}
