package com.hccake.ballcat.log.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.log.mapper.OperationLogMapper;
import com.hccake.ballcat.log.model.entity.OperationLog;
import com.hccake.ballcat.log.model.qo.OperationLogQO;
import com.hccake.ballcat.log.model.vo.OperationLogPageVO;
import com.hccake.ballcat.log.service.OperationLogService;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 操作日志
 *
 * @author hccake
 * @date 2019-10-15 20:42:32
 */
@Service
public class OperationLogServiceImpl extends ExtendServiceImpl<OperationLogMapper, OperationLog>
		implements OperationLogService {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<LoginLogVO> 分页数据
	 */
	@Override
	public PageResult<OperationLogPageVO> queryPage(PageParam pageParam, OperationLogQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	/**
	 * 异步保存操作日志
	 * @param operationLog 操作日志
	 */
	@Async
	@Override
	public void saveAsync(OperationLog operationLog) {
		baseMapper.insert(operationLog);
	}

}
