package com.hccake.ballcat.log.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hccake.ballcat.log.converter.LoginLogConverter;
import com.hccake.ballcat.log.model.entity.LoginLog;
import com.hccake.ballcat.log.model.qo.LoginLogQO;
import com.hccake.ballcat.log.model.vo.LoginLogPageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

/**
 * 登陆日志
 *
 * @author hccake 2020-09-16 20:21:10
 */
public interface LoginLogMapper extends ExtendMapper<LoginLog> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return 分页结果数据 PageResult
	 */
	default PageResult<LoginLogPageVO> queryPage(PageParam pageParam, LoginLogQO qo) {
		IPage<LoginLog> page = this.prodPage(pageParam);
		LambdaQueryWrapperX<LoginLog> wrapperX = WrappersX.lambdaQueryX(LoginLog.class)
				.eqIfPresent(LoginLog::getUsername, qo.getUsername()).eqIfPresent(LoginLog::getTraceId, qo.getTraceId())
				.eqIfPresent(LoginLog::getIp, qo.getIp()).eqIfPresent(LoginLog::getEventType, qo.getEventType())
				.eqIfPresent(LoginLog::getStatus, qo.getStatus()).gtIfPresent(LoginLog::getLoginTime, qo.getStartTime())
				.ltIfPresent(LoginLog::getLoginTime, qo.getEndTime());
		this.selectPage(page, wrapperX);
		IPage<LoginLogPageVO> voPage = page.convert(LoginLogConverter.INSTANCE::poToPageVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

}