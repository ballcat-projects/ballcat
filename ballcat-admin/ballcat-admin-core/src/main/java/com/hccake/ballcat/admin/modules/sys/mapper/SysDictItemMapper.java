package com.hccake.ballcat.admin.modules.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysDictItem;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysDictItemVO;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.domain.PageResult;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;

/**
 * 字典项
 *
 * @author hccake
 * @date 2020-03-26 18:40:20
 */
public interface SysDictItemMapper extends ExtendMapper<SysDictItem> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param dictCode 字典标识
	 * @return PageResult<SysRoleVO>
	 */
	default PageResult<SysDictItemVO> queryPage(PageParam pageParam, String dictCode) {
		IPage<SysDictItemVO> page = this.prodPage(pageParam);
		LambdaQueryWrapper<SysDictItem> wrapper = Wrappers.lambdaQuery(SysDictItem.class).eq(SysDictItem::getDictCode,
				dictCode);
		this.selectByPage(page, wrapper);
		return new PageResult<>(page.getRecords(), page.getTotal());
	}

}
