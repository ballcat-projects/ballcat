package com.hccake.ballcat.admin.modules.log.service;

import com.hccake.ballcat.admin.modules.log.model.entity.AdminLoginLog;
import com.hccake.ballcat.admin.modules.log.model.qo.AdminLoginLogQO;
import com.hccake.ballcat.admin.modules.log.model.vo.AdminLoginLogVO;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;

/**
 * 登陆日志
 *
 * @author hccake 2020-09-16 20:21:10
 */
public interface AdminLoginLogService extends ExtendService<AdminLoginLog> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param page 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<LoginLogVO> 分页数据
	 */
	PageResult<AdminLoginLogVO> queryPage(PageParam page, AdminLoginLogQO qo);

}