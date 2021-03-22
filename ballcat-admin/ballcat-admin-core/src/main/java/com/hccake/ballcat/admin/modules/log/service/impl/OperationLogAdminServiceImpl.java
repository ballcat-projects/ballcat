package com.hccake.ballcat.admin.modules.log.service.impl;

import com.hccake.ballcat.admin.modules.log.mapper.AdminOperationLogMapper;
import com.hccake.ballcat.admin.modules.log.model.entity.AdminOperationLog;
import com.hccake.ballcat.admin.modules.log.model.qo.AdminOperationLogQO;
import com.hccake.ballcat.admin.modules.log.model.vo.AdminOperationLogPageVO;
import com.hccake.ballcat.admin.modules.log.service.OperationLogAdminService;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 操作日志
 *
 * @author hccake
 * @date 2019-10-15 20:42:32
 */
@Service
public class OperationLogAdminServiceImpl extends ExtendServiceImpl<AdminOperationLogMapper, AdminOperationLog>
		implements OperationLogAdminService {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<LoginLogVO> 分页数据
	 */
	@Override
	public PageResult<AdminOperationLogPageVO> queryPage(PageParam pageParam, AdminOperationLogQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

}
