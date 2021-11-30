package com.hccake.ballcat.log.service.impl;

import com.hccake.ballcat.log.mapper.LoginLogMapper;
import com.hccake.ballcat.log.model.entity.LoginLog;
import com.hccake.ballcat.log.model.qo.LoginLogQO;
import com.hccake.ballcat.log.model.vo.LoginLogPageVO;
import com.hccake.ballcat.log.service.LoginLogService;
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
public class LoginLogServiceImpl extends ExtendServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<LoginLogVO> 分页数据
	 */
	@Override
	public PageResult<LoginLogPageVO> queryPage(PageParam pageParam, LoginLogQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

}
