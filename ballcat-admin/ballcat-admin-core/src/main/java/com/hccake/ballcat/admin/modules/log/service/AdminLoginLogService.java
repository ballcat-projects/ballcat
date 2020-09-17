package com.hccake.ballcat.admin.modules.log.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hccake.ballcat.admin.modules.log.model.entity.AdminLoginLog;
import com.hccake.ballcat.admin.modules.log.model.vo.AdminLoginLogVO;
import com.hccake.ballcat.admin.modules.log.model.qo.AdminLoginLogQO;

/**
 * 登陆日志
 *
 * @author hccake 2020-09-16 20:21:10
 */
public interface AdminLoginLogService extends IService<AdminLoginLog> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param page 分页参数
	 * @param qo 查询参数对象
	 * @return IPage<LoginLogVO> 分页数据
	 */
	IPage<AdminLoginLogVO> selectPageVo(IPage<?> page, AdminLoginLogQO qo);

}