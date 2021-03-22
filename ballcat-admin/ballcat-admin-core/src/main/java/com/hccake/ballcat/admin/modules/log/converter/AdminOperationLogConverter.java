package com.hccake.ballcat.admin.modules.log.converter;

import com.hccake.ballcat.admin.modules.log.model.entity.AdminOperationLog;
import com.hccake.ballcat.admin.modules.log.model.vo.AdminOperationLogPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 操作日志模型转换器
 *
 * @author hccake 2021-03-22 20:32:30
 */
@Mapper
public interface AdminOperationLogConverter {

	AdminOperationLogConverter INSTANCE = Mappers.getMapper(AdminOperationLogConverter.class);

	/**
	 * PO 转 PageVO
	 * @param adminOperationLog 操作日志
	 * @return AdminOperationLogPageVO 操作日志PageVO
	 */
	AdminOperationLogPageVO poToPageVo(AdminOperationLog adminOperationLog);

}
