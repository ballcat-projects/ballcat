package com.hccake.ballcat.admin.modules.sys.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysDict;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysDictQO;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysDictVO;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.domain.PageResult;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

import java.util.List;

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
		LambdaQueryWrapperX<SysDict> wrapper = WrappersX.lambdaQueryX(SysDict.class)
				.likeIfPresent(SysDict::getCode, qo.getCode()).likeIfPresent(SysDict::getTitle, qo.getTitle());
		this.selectByPage(page, wrapper);
		return new PageResult<>(page.getRecords(), page.getTotal());
	}

	/**
	 * 根据字典标识查询对应字典
	 * @param dictCode 字典标识
	 * @return SysDict 字典
	 */
	default SysDict getByCode(String dictCode) {
		return this.selectOne(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getCode, dictCode));
	}

	/**
	 * 根据字典标识数组查询对应字典集合
	 * @param dictCodes 字典标识数组
	 * @return List<SysDict> 字典集合
	 */
	default List<SysDict> listByCodes(String[] dictCodes) {
		return this.selectList(Wrappers.<SysDict>lambdaQuery().in(SysDict::getCode, (Object[]) dictCodes));
	}

	/**
	 * 更新字典的HashCode
	 * @param dictCode 字典标识
	 * @param hashCode 哈希值
	 * @return boolean 是否更新成功
	 */
	default boolean updateHashCode(String dictCode, String hashCode) {
		int flag = this.update(null,
				Wrappers.<SysDict>lambdaUpdate().set(SysDict::getHashCode, hashCode).eq(SysDict::getCode, dictCode));
		return SqlHelper.retBool(flag);
	}

}
