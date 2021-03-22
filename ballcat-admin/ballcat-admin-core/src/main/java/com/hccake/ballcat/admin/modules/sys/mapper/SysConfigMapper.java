package com.hccake.ballcat.admin.modules.sys.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.admin.modules.sys.converter.SysConfigConverter;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysConfig;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysConfigQO;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysConfigPageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;

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
		this.selectPage(page, Wrappers.emptyWrapper());
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

}
