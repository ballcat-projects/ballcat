package com.hccake.ballcat.admin.modules.log.converter;

import com.hccake.ballcat.admin.modules.log.model.entity.AdminLoginLog;
import com.hccake.ballcat.admin.modules.log.model.vo.AdminLoginLogPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 登陆日志模型转换器
 *
 * @author hccake 2021-03-22 20:28:16
 */
@Mapper
public interface AdminLoginLogConverter {

	AdminLoginLogConverter INSTANCE = Mappers.getMapper(AdminLoginLogConverter.class);

	/**
	 * PO 转 PageVO
	 * @param adminLoginLog 登陆日志
	 * @return AdminLoginLogPageVO 登陆日志PageVO
	 */
	AdminLoginLogPageVO poToPageVo(AdminLoginLog adminLoginLog);

}
