package com.hccake.ballcat.log.service;

import com.hccake.ballcat.log.model.entity.LoginLog;
import com.hccake.ballcat.log.model.qo.LoginLogQO;
import com.hccake.ballcat.log.model.vo.LoginLogPageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;

/**
 * 登陆日志
 *
 * @author hccake 2020-09-16 20:21:10
 */
public interface LoginLogService extends ExtendService<LoginLog> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param page 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<LoginLogVO> 分页数据
	 */
	PageResult<LoginLogPageVO> queryPage(PageParam page, LoginLogQO qo);

}