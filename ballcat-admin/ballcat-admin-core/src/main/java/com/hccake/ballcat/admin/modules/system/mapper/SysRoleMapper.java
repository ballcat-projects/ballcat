package com.hccake.ballcat.admin.modules.system.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.admin.modules.system.converter.SysRoleConverter;
import com.hccake.ballcat.admin.modules.system.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.system.model.qo.SysRoleQO;
import com.hccake.ballcat.admin.modules.system.model.vo.SysRolePageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.domain.SelectData;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author ballcat
 * @since 2017-10-29
 */
public interface SysRoleMapper extends ExtendMapper<SysRole> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return PageResult<SysRoleVO>
	 */
	default PageResult<SysRolePageVO> queryPage(PageParam pageParam, SysRoleQO qo) {
		IPage<SysRole> page = this.prodPage(pageParam);
		LambdaQueryWrapper<SysRole> wrapper = Wrappers.<SysRole>lambdaQuery()
				.like(StrUtil.isNotBlank(qo.getName()), SysRole::getName, qo.getName())
				.like(StrUtil.isNotBlank(qo.getCode()), SysRole::getCode, qo.getCode())
				.between(StrUtil.isNotBlank(qo.getStartTime()) && StrUtil.isNotBlank(qo.getEndTime()),
						SysRole::getCreateTime, qo.getStartTime(), qo.getEndTime());
		this.selectPage(page, wrapper);
		IPage<SysRolePageVO> voPage = page.convert(SysRoleConverter.INSTANCE::poToPageVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	/**
	 * 获取角色下拉框数据
	 * @return 下拉选择框数据集合
	 */
	List<SelectData<?>> listSelectData();

}
