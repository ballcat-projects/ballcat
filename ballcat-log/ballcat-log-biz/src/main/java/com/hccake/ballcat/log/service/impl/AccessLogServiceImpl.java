package com.hccake.ballcat.log.service.impl;

import com.hccake.ballcat.log.mapper.AccessLogMapper;
import com.hccake.ballcat.log.model.entity.AccessLog;
import com.hccake.ballcat.log.model.qo.AccessLogQO;
import com.hccake.ballcat.log.model.vo.AccessLogPageVO;
import com.hccake.ballcat.log.service.AccessLogService;
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
public class AccessLogServiceImpl extends ExtendServiceImpl<AccessLogMapper, AccessLog> implements AccessLogService {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return IPage<LoginLogVO> 分页数据
	 */
	@Override
	public PageResult<AccessLogPageVO> queryPage(PageParam pageParam, AccessLogQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

}
