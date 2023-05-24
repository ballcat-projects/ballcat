package com.hccake.ballcat.log.converter;

import com.hccake.ballcat.log.model.entity.LoginLog;
import com.hccake.ballcat.log.model.vo.LoginLogPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 登录日志模型转换器
 *
 * @author hccake 2021-03-22 20:28:16
 */
@Mapper
public interface LoginLogConverter {

	LoginLogConverter INSTANCE = Mappers.getMapper(LoginLogConverter.class);

	/**
	 * PO 转 PageVO
	 * @param loginLog 登录日志
	 * @return AdminLoginLogPageVO 登录日志PageVO
	 */
	LoginLogPageVO poToPageVo(LoginLog loginLog);

}
