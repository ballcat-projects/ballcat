package com.hccake.ballcat.admin.modules.log.service;

import com.hccake.ballcat.admin.modules.log.model.entity.AdminOperationLog;
import com.hccake.ballcat.admin.modules.log.model.qo.AdminOperationLogQO;
import com.hccake.ballcat.admin.modules.log.model.vo.AdminOperationLogVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;

/**
 * 操作日志
 *
 * @author hccake
 * @date 2019-10-15 20:42:32
 */
public interface OperationLogAdminService extends ExtendService<AdminOperationLog> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<LoginLogVO> 分页数据
	 */
	PageResult<AdminOperationLogVO> queryPage(PageParam pageParam, AdminOperationLogQO qo);

}
