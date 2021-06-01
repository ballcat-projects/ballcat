package com.hccake.ballcat.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.system.converter.SysConfigConverter;
import com.hccake.ballcat.system.model.entity.SysConfig;
import com.hccake.ballcat.system.model.qo.SysConfigQO;
import com.hccake.ballcat.system.model.vo.SysConfigPageVO;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

/**
 * 系统配置表
 *
 * @author ballcat code generator
 * @date 2019-10-14 17:42:23
 */
public interface SysConfigMapper extends ExtendMapper<SysConfig> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param sysConfigQO 查询参数
	 * @return PageResult<SysRoleVO>
	 */
	default PageResult<SysConfigPageVO> queryPage(PageParam pageParam, SysConfigQO sysConfigQO) {
		IPage<SysConfig> page = this.prodPage(pageParam);
		Wrapper<SysConfig> wrapper = WrappersX.lambdaQueryX(SysConfig.class)
				.likeIfPresent(SysConfig::getConfKey, sysConfigQO.getConfKey())
				.likeIfPresent(SysConfig::getName, sysConfigQO.getName())
				.likeIfPresent(SysConfig::getCategory, sysConfigQO.getCategory());
		this.selectPage(page, wrapper);
		IPage<SysConfigPageVO> voPage = page.convert(SysConfigConverter.INSTANCE::poToPageVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	/**
	 * 根据配置key查询配置信息
	 * @param confKey 配置key
	 * @return SysConfig 配置信息
	 */
	default SysConfig selectByKey(String confKey) {
		return this.selectOne(Wrappers.<SysConfig>lambdaQuery().eq(SysConfig::getConfKey, confKey));
	}

	/**
	 * 根据 confKey 进行更新
	 * @param sysConfig 系统配置
	 * @return 更新是否成功
	 */
	default boolean updateByKey(SysConfig sysConfig) {
		Wrapper<SysConfig> wrapper = Wrappers.lambdaUpdate(SysConfig.class).eq(SysConfig::getConfKey,
				sysConfig.getConfKey());
		int flag = this.update(sysConfig, wrapper);
		return SqlHelper.retBool(flag);
	}

	/**
	 * 根据 confKey 进行删除
	 * @param confKey 配置key
	 * @return 删除是否成功
	 */
	default boolean deleteByKey(String confKey) {
		Wrapper<SysConfig> wrapper = Wrappers.lambdaQuery(SysConfig.class).eq(SysConfig::getConfKey, confKey);
		int flag = this.delete(wrapper);
		return SqlHelper.retBool(flag);
	}

}
