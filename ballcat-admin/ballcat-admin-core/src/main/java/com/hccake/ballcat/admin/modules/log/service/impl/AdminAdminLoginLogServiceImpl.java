package com.hccake.ballcat.admin.modules.log.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.ballcat.admin.modules.log.mapper.AdminLoginLogMapper;
import com.hccake.ballcat.admin.modules.log.model.entity.AdminLoginLog;
import com.hccake.ballcat.admin.modules.log.model.qo.AdminLoginLogQO;
import com.hccake.ballcat.admin.modules.log.model.vo.AdminLoginLogVO;
import com.hccake.ballcat.admin.modules.log.service.AdminLoginLogService;
import org.springframework.stereotype.Service;

/**
 * 登陆日志
 *
 * @author hccake 2020-09-16 20:21:10
 */
@Service
public class AdminAdminLoginLogServiceImpl extends ServiceImpl<AdminLoginLogMapper, AdminLoginLog>
		implements AdminLoginLogService {

	private final static String TABLE_ALIAS_PREFIX = "ll.";

	/**
	 * 根据QueryObject查询分页数据
	 * @param page 分页参数
	 * @param qo 查询参数对象
	 * @return IPage<LoginLogVO> 分页数据
	 */
	@Override
	public IPage<AdminLoginLogVO> selectPageVo(IPage<?> page, AdminLoginLogQO qo) {
		QueryWrapper<AdminLoginLog> wrapper = Wrappers.<AdminLoginLog>query()
				.eq(StrUtil.isNotBlank(qo.getUsername()), TABLE_ALIAS_PREFIX + "username", qo.getUsername())
				.eq(StrUtil.isNotBlank(qo.getTraceId()), TABLE_ALIAS_PREFIX + "trace_id", qo.getTraceId())
				.eq(StrUtil.isNotBlank(qo.getIp()), TABLE_ALIAS_PREFIX + "ip", qo.getIp())
				.eq(ObjectUtil.isNotNull(qo.getEventType()), TABLE_ALIAS_PREFIX + "event_type", qo.getEventType())
				.eq(ObjectUtil.isNotNull(qo.getStatus()), TABLE_ALIAS_PREFIX + "status", qo.getStatus())
				.gt(ObjectUtil.isNotNull(qo.getStartTime()), TABLE_ALIAS_PREFIX + "login_time", qo.getStartTime())
				.lt(ObjectUtil.isNotNull(qo.getEndTime()), TABLE_ALIAS_PREFIX + "login_time", qo.getEndTime());
		return baseMapper.selectPageVo(page, wrapper);
	}

}
