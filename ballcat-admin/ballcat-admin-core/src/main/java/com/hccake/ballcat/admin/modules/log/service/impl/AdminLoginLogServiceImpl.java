package com.hccake.ballcat.admin.modules.log.service.impl;

import com.hccake.ballcat.admin.modules.log.mapper.AdminLoginLogMapper;
import com.hccake.ballcat.admin.modules.log.model.entity.AdminLoginLog;
import com.hccake.ballcat.admin.modules.log.model.qo.AdminLoginLogQO;
import com.hccake.ballcat.admin.modules.log.model.vo.AdminLoginLogPageVO;
import com.hccake.ballcat.admin.modules.log.service.AdminLoginLogService;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 登陆日志
 *
 * @author hccake 2020-09-16 20:21:10
 */
@Service
public class AdminLoginLogServiceImpl extends ExtendServiceImpl<AdminLoginLogMapper, AdminLoginLog>
		implements AdminLoginLogService {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<LoginLogVO> 分页数据
	 */
	@Override
	public PageResult<AdminLoginLogPageVO> queryPage(PageParam pageParam, AdminLoginLogQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

}
