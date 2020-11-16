package com.hccake.ballcat.admin.modules.sys.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.admin.modules.sys.mapper.SysDictMapper;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysDict;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysDictQO;
import com.hccake.ballcat.admin.modules.sys.service.SysDictService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典表
 *
 * @author hccake
 * @date 2020-03-26 18:40:20
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

	/**
	 * 根据QueryObeject查询分页数据
	 * @param page 分页参数
	 * @param qo 查询参数对象
	 * @return 分页数据
	 */
	@Override
	public IPage<SysDict> page(IPage<SysDict> page, SysDictQO qo) {
		LambdaQueryWrapper<SysDict> wrapper = Wrappers.<SysDict>lambdaQuery()
				.like(StrUtil.isNotBlank(qo.getCode()), SysDict::getCode, qo.getCode())
				.like(StrUtil.isNotBlank(qo.getTitle()), SysDict::getTitle, qo.getTitle());
		return baseMapper.selectPage(page, wrapper);
	}

	/**
	 * 根据字典标识查询
	 * @param dictCode 字典标识
	 * @return 字典数据
	 */
	@Override
	public SysDict getByCode(String dictCode) {
		return baseMapper.selectOne(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getCode, dictCode));
	}

	/**
	 * 根据字典标识查询
	 * @param dictCode 字典标识
	 * @return 字典数据
	 */
	@Override
	public List<SysDict> getByCode(String[] dictCode) {
		if (dictCode == null || dictCode.length == 0) {
			return new ArrayList<>();
		}
		return baseMapper.selectList(Wrappers.<SysDict>lambdaQuery().in(SysDict::getCode, dictCode));
	}

	/**
	 * 更新字典HashCode
	 * @param dictCode 字典标识
	 * @return 更新状态: 成功(true) 失败(false)
	 */
	@Override
	public boolean updateHashCode(String dictCode) {
		int flag = baseMapper.update(null, Wrappers.<SysDict>lambdaUpdate()
				.set(SysDict::getHashCode, IdUtil.fastSimpleUUID()).eq(SysDict::getCode, dictCode));
		return SqlHelper.retBool(flag);
	}

}
