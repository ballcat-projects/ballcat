package com.hccake.ballcat.admin.modules.sys.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysRoleQO;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysRoleVO;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.domain.PageResult;
import com.hccake.ballcat.common.core.domain.SelectData;
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
	default PageResult<SysRoleVO> queryPage(PageParam pageParam, SysRoleQO qo) {
		IPage<SysRoleVO> page = this.prodPage(pageParam);
		LambdaQueryWrapper<SysRole> wrapper = Wrappers.<SysRole>lambdaQuery()
				.like(StrUtil.isNotBlank(qo.getName()), SysRole::getName, qo.getName())
				.like(StrUtil.isNotBlank(qo.getCode()), SysRole::getCode, qo.getCode())
				.between(StrUtil.isNotBlank(qo.getStartTime()) && StrUtil.isNotBlank(qo.getEndTime()),
						SysRole::getCreateTime, qo.getStartTime(), qo.getEndTime());
		this.selectByPage(page, wrapper);
		return new PageResult<>(page.getRecords(), page.getTotal());
	}

	/**
	 * 获取角色下拉框数据
	 * @return 下拉选择框数据集合
	 */
	List<SelectData<?>> listSelectData();

}
