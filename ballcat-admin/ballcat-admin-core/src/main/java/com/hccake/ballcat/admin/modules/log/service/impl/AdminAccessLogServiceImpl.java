package com.hccake.ballcat.admin.modules.log.service.impl;

import com.hccake.ballcat.admin.modules.log.mapper.AdminAccessLogMapper;
import com.hccake.ballcat.admin.modules.log.model.entity.AdminAccessLog;
import com.hccake.ballcat.admin.modules.log.model.qo.AdminAccessLogQO;
import com.hccake.ballcat.admin.modules.log.model.vo.AdminAccessLogVO;
import com.hccake.ballcat.admin.modules.log.service.AdminAccessLogService;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 后台访问日志
 *
 * @author hccake
 * @date 2019-10-16 16:09:25
 */
@Slf4j
@Service
public class AdminAccessLogServiceImpl extends ExtendServiceImpl<AdminAccessLogMapper, AdminAccessLog>
		implements AdminAccessLogService {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return IPage<LoginLogVO> 分页数据
	 */
	@Override
	public PageResult<AdminAccessLogVO> queryPage(PageParam pageParam, AdminAccessLogQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

}
