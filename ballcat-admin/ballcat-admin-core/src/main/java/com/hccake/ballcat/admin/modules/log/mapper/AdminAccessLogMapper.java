package com.hccake.ballcat.admin.modules.log.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hccake.ballcat.admin.modules.log.converter.AdminAccessLogConverter;
import com.hccake.ballcat.admin.modules.log.model.entity.AdminAccessLog;
import com.hccake.ballcat.admin.modules.log.model.qo.AdminAccessLogQO;
import com.hccake.ballcat.admin.modules.log.model.vo.AdminAccessLogPageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

/**
 * 后台访问日志
 *
 * @author hccake
 * @date 2019-10-16 16:09:25
 */
public interface AdminAccessLogMapper extends ExtendMapper<AdminAccessLog> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return 分页结果数据 PageResult
	 */
	default PageResult<AdminAccessLogPageVO> queryPage(PageParam pageParam, AdminAccessLogQO qo) {
		IPage<AdminAccessLog> page = this.prodPage(pageParam);
		LambdaQueryWrapperX<AdminAccessLog> wrapperX = WrappersX.lambdaQueryX(AdminAccessLog.class)
				.eqIfPresent(AdminAccessLog::getUserId, qo.getUserId())
				.eqIfPresent(AdminAccessLog::getTraceId, qo.getTraceId())
				.eqIfPresent(AdminAccessLog::getMatchingPattern, qo.getMatchingPattern())
				.eqIfPresent(AdminAccessLog::getIp, qo.getIp())
				.gtIfPresent(AdminAccessLog::getCreateTime, qo.getStartTime())
				.ltIfPresent(AdminAccessLog::getCreateTime, qo.getEndTime());
		this.selectPage(page, wrapperX);
		IPage<AdminAccessLogPageVO> voPage = page.convert(AdminAccessLogConverter.INSTANCE::poToPageVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

}
