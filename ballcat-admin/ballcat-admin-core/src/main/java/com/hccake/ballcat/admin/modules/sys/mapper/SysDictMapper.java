package com.hccake.ballcat.admin.modules.sys.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysDict;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysDictQO;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysDictVO;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.domain.PageResult;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;

/**
 * 字典表
 *
 * @author hccake
 * @date 2020-03-26 18:40:20
 */
public interface SysDictMapper extends ExtendMapper<SysDict> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return PageResult<SysRoleVO>
	 */
	default PageResult<SysDictVO> queryPage(PageParam pageParam, SysDictQO qo) {
		IPage<SysDictVO> page = this.prodPage(pageParam);
		LambdaQueryWrapper<SysDict> wrapper = Wrappers.lambdaQuery(SysDict.class)
				.like(StrUtil.isNotBlank(qo.getCode()), SysDict::getCode, qo.getCode())
				.like(StrUtil.isNotBlank(qo.getTitle()), SysDict::getTitle, qo.getTitle());
		this.selectByPage(page, wrapper);
		return new PageResult<>(page.getRecords(), page.getTotal());
	}

}
