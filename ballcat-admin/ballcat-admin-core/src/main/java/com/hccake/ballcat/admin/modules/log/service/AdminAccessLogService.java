package com.hccake.ballcat.admin.modules.log.service;

import com.hccake.ballcat.admin.modules.log.model.entity.AdminAccessLog;
import com.hccake.ballcat.admin.modules.log.model.qo.AdminAccessLogQO;
import com.hccake.ballcat.admin.modules.log.model.vo.AdminAccessLogVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;

/**
 * 后台访问日志
 *
 * @author hccake
 * @date 2019-10-16 16:09:25
 */
public interface AdminAccessLogService extends ExtendService<AdminAccessLog> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param page 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<LoginLogVO> 分页数据
	 */
	PageResult<AdminAccessLogVO> queryPage(PageParam page, AdminAccessLogQO qo);

}
