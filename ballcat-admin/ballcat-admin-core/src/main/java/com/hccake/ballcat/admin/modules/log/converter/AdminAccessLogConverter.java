package com.hccake.ballcat.admin.modules.log.converter;

import com.hccake.ballcat.admin.modules.log.model.entity.AdminAccessLog;
import com.hccake.ballcat.admin.modules.log.model.vo.AdminAccessLogPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 访问日志
 *
 * @author hccake 2021-03-22 20:23:41
 */
@Mapper
public interface AdminAccessLogConverter {

	AdminAccessLogConverter INSTANCE = Mappers.getMapper(AdminAccessLogConverter.class);

	/**
	 * PO 转 PageVO
	 * @param adminAccessLog 访问日志
	 * @return AdminAccessLogVO 访问日志VO
	 */
	AdminAccessLogPageVO poToPageVo(AdminAccessLog adminAccessLog);

}
