package com.hccake.ballcat.admin.modules.log.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hccake.ballcat.admin.modules.log.model.entity.AdminLoginLog;
import com.hccake.ballcat.admin.modules.log.model.qo.AdminLoginLogQO;
import com.hccake.ballcat.admin.modules.log.model.vo.AdminLoginLogVO;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.domain.PageResult;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

/**
 * 登陆日志
 *
 * @author hccake 2020-09-16 20:21:10
 */
public interface AdminLoginLogMapper extends ExtendMapper<AdminLoginLog> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return 分页结果数据 PageResult
	 */
	default PageResult<AdminLoginLogVO> queryPage(PageParam pageParam, AdminLoginLogQO qo) {
		IPage<AdminLoginLogVO> page = this.prodPage(pageParam);
		LambdaQueryWrapperX<AdminLoginLog> wrapperX = WrappersX.lambdaQueryX(AdminLoginLog.class)
				.eqIfPresent(AdminLoginLog::getUsername, qo.getUsername())
				.eqIfPresent(AdminLoginLog::getTraceId, qo.getTraceId()).eqIfPresent(AdminLoginLog::getIp, qo.getIp())
				.eqIfPresent(AdminLoginLog::getEventType, qo.getEventType())
				.eqIfPresent(AdminLoginLog::getStatus, qo.getStatus())
				.gtIfPresent(AdminLoginLog::getLoginTime, qo.getStartTime())
				.ltIfPresent(AdminLoginLog::getLoginTime, qo.getEndTime());
		this.selectByPage(page, wrapperX);
		return new PageResult<>(page.getRecords(), page.getTotal());
	}

}