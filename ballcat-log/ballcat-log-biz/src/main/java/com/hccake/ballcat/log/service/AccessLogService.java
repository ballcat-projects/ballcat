package com.hccake.ballcat.log.service;

import com.hccake.ballcat.log.model.entity.AccessLog;
import com.hccake.ballcat.log.model.qo.AccessLogQO;
import com.hccake.ballcat.log.model.vo.AccessLogPageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;

/**
 * 后台访问日志
 *
 * @author hccake
 * @date 2019-10-16 16:09:25
 */
public interface AccessLogService extends ExtendService<AccessLog> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param page 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<LoginLogVO> 分页数据
	 */
	PageResult<AccessLogPageVO> queryPage(PageParam page, AccessLogQO qo);

}
