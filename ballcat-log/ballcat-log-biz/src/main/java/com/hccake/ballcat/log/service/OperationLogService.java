package com.hccake.ballcat.log.service;

import com.hccake.ballcat.log.model.entity.OperationLog;
import com.hccake.ballcat.log.model.qo.OperationLogQO;
import com.hccake.ballcat.log.model.vo.OperationLogPageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;

/**
 * 操作日志
 *
 * @author hccake
 * @date 2019-10-15 20:42:32
 */
public interface OperationLogService extends ExtendService<OperationLog> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<LoginLogVO> 分页数据
	 */
	PageResult<OperationLogPageVO> queryPage(PageParam pageParam, OperationLogQO qo);

	/**
	 * 异步保存操作日志
	 * @param operationLog 操作日志
	 */
	void saveAsync(OperationLog operationLog);

}
