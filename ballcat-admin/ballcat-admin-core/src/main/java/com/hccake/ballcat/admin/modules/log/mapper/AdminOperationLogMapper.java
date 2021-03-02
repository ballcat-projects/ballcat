package com.hccake.ballcat.admin.modules.log.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hccake.ballcat.admin.modules.log.model.entity.AdminOperationLog;
import com.hccake.ballcat.admin.modules.log.model.qo.AdminOperationLogQO;
import com.hccake.ballcat.admin.modules.log.model.vo.AdminOperationLogVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

/**
 * 操作日志
 *
 * @author hccake
 * @date 2019-10-15 20:42:32
 */
public interface AdminOperationLogMapper extends ExtendMapper<AdminOperationLog> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return 分页结果数据 PageResult
	 */
	default PageResult<AdminOperationLogVO> queryPage(PageParam pageParam, AdminOperationLogQO qo) {
		IPage<AdminOperationLogVO> page = this.prodPage(pageParam);
		LambdaQueryWrapperX<AdminOperationLog> wrapperX = WrappersX.lambdaQueryX(AdminOperationLog.class)
				.eqIfPresent(AdminOperationLog::getOperator, qo.getUserId())
				.eqIfPresent(AdminOperationLog::getTraceId, qo.getTraceId())
				.eqIfPresent(AdminOperationLog::getUri, qo.getUri()).eqIfPresent(AdminOperationLog::getIp, qo.getIp())
				.eqIfPresent(AdminOperationLog::getStatus, qo.getStatus())
				.eqIfPresent(AdminOperationLog::getType, qo.getType())
				.gtIfPresent(AdminOperationLog::getCreateTime, qo.getStartTime())
				.ltIfPresent(AdminOperationLog::getCreateTime, qo.getEndTime());
		this.selectByPage(page, wrapperX);
		return new PageResult<>(page.getRecords(), page.getTotal());
	}

}
