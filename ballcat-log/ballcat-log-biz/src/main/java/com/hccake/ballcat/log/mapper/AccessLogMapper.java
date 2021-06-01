package com.hccake.ballcat.log.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hccake.ballcat.log.converter.AccessLogConverter;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.log.model.entity.AccessLog;
import com.hccake.ballcat.log.model.qo.AccessLogQO;
import com.hccake.ballcat.log.model.vo.AccessLogPageVO;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

/**
 * 后台访问日志
 *
 * @author hccake
 * @date 2019-10-16 16:09:25
 */
public interface AccessLogMapper extends ExtendMapper<AccessLog> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return 分页结果数据 PageResult
	 */
	default PageResult<AccessLogPageVO> queryPage(PageParam pageParam, AccessLogQO qo) {
		IPage<AccessLog> page = this.prodPage(pageParam);
		LambdaQueryWrapperX<AccessLog> wrapperX = WrappersX.lambdaQueryX(AccessLog.class)
				.eqIfPresent(AccessLog::getUserId, qo.getUserId()).eqIfPresent(AccessLog::getTraceId, qo.getTraceId())
				.eqIfPresent(AccessLog::getMatchingPattern, qo.getMatchingPattern())
				.eqIfPresent(AccessLog::getUri, qo.getUri()).eqIfPresent(AccessLog::getHttpStatus, qo.getHttpStatus())
				.eqIfPresent(AccessLog::getIp, qo.getIp()).gtIfPresent(AccessLog::getCreateTime, qo.getStartTime())
				.ltIfPresent(AccessLog::getCreateTime, qo.getEndTime());
		this.selectPage(page, wrapperX);
		IPage<AccessLogPageVO> voPage = page.convert(AccessLogConverter.INSTANCE::poToPageVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

}
