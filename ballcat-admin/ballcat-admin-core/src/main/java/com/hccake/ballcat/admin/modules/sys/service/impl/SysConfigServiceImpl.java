package com.hccake.ballcat.admin.modules.sys.service.impl;

import com.hccake.ballcat.admin.modules.sys.mapper.SysConfigMapper;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysConfig;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysConfigQO;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysConfigPageVO;
import com.hccake.ballcat.admin.modules.sys.service.SysConfigService;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 系统配置表
 *
 * @author ballcat code generator
 * @date 2019-10-14 17:42:23
 */
@Service
public class SysConfigServiceImpl extends ExtendServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param sysConfigQO 查询参数对象
	 * @return 分页数据
	 */
	@Override
	public PageResult<SysConfigPageVO> queryPage(PageParam pageParam, SysConfigQO sysConfigQO) {
		return baseMapper.queryPage(pageParam, sysConfigQO);
	}

	/**
	 * 根据配置key获取对应value
	 * @param confKey 缓存对应key
	 * @return confValue
	 */
	@Override
	public String getConfValueByKey(String confKey) {
		SysConfig sysConfig = baseMapper.selectByKey(confKey);
		return sysConfig == null ? "" : sysConfig.getConfValue();
	}

}
