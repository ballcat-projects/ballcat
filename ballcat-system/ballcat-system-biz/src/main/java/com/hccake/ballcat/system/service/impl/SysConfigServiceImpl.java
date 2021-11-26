package com.hccake.ballcat.system.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.redis.core.annotation.CacheDel;
import com.hccake.ballcat.common.redis.core.annotation.Cached;
import com.hccake.ballcat.system.constant.SystemRedisKeyConstants;
import com.hccake.ballcat.system.mapper.SysConfigMapper;
import com.hccake.ballcat.system.model.entity.SysConfig;
import com.hccake.ballcat.system.model.qo.SysConfigQO;
import com.hccake.ballcat.system.model.vo.SysConfigPageVO;
import com.hccake.ballcat.system.service.SysConfigService;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

/**
 * 系统配置表
 *
 * @author ballcat code generator
 * @date 2019-10-14 17:42:23
 */
@Service
@ConditionalOnMissingBean(SysConfigService.class)
public class SysConfigServiceImpl extends ExtendServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

	@Override
	public PageResult<SysConfigPageVO> queryPage(PageParam pageParam, SysConfigQO sysConfigQO) {
		return baseMapper.queryPage(pageParam, sysConfigQO);
	}

	@Cached(key = SystemRedisKeyConstants.SYSTEM_CONFIG_PREFIX, keyJoint = "#confKey")
	@Override
	public String getConfValueByKey(String confKey) {
		SysConfig sysConfig = baseMapper.selectByKey(confKey);
		return sysConfig == null ? null : sysConfig.getConfValue();
	}

	/**
	 * 保存系统配置，由于查询不到时会缓存空值，所以新建时也需要删除对应 key，防止之前误存了空值数据
	 * @param entity 实体对象
	 * @return 保存成功 true
	 */
	@CacheDel(key = SystemRedisKeyConstants.SYSTEM_CONFIG_PREFIX, keyJoint = "#p0.confKey")
	@Override
	public boolean save(SysConfig entity) {
		return SqlHelper.retBool(getBaseMapper().insert(entity));
	}

	@CacheDel(key = SystemRedisKeyConstants.SYSTEM_CONFIG_PREFIX, keyJoint = "#sysConfig.confKey")
	@Override
	public boolean updateByKey(SysConfig sysConfig) {
		return baseMapper.updateByKey(sysConfig);
	}

	@CacheDel(key = SystemRedisKeyConstants.SYSTEM_CONFIG_PREFIX, keyJoint = "#confKey")
	@Override
	public boolean removeByKey(String confKey) {
		return baseMapper.deleteByKey(confKey);
	}

}
