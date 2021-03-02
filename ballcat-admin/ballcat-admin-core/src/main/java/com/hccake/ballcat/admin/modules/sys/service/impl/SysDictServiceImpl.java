package com.hccake.ballcat.admin.modules.sys.service.impl;

import cn.hutool.core.util.IdUtil;
import com.hccake.ballcat.admin.modules.sys.mapper.SysDictMapper;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysDict;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysDictQO;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysDictVO;
import com.hccake.ballcat.admin.modules.sys.service.SysDictService;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
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
public class SysDictServiceImpl extends ExtendServiceImpl<SysDictMapper, SysDict> implements SysDictService {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<SysDictVO> 分页数据
	 */
	@Override
	public PageResult<SysDictVO> queryPage(PageParam pageParam, SysDictQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	/**
	 * 根据字典标识查询
	 * @param dictCode 字典标识
	 * @return 字典数据
	 */
	@Override
	public SysDict getByCode(String dictCode) {
		return baseMapper.getByCode(dictCode);
	}

	/**
	 * 根据字典标识数组查询对应字典集合
	 * @param dictCodes 字典标识数组
	 * @return List<SysDict> 字典集合
	 */
	@Override
	public List<SysDict> listByCodes(String[] dictCodes) {
		if (dictCodes == null || dictCodes.length == 0) {
			return new ArrayList<>();
		}
		return baseMapper.listByCodes(dictCodes);
	}

	/**
	 * 更新字典HashCode
	 * @param dictCode 字典标识
	 * @return 更新状态: 成功(true) 失败(false)
	 */
	@Override
	public boolean updateHashCode(String dictCode) {
		return baseMapper.updateHashCode(dictCode, IdUtil.fastSimpleUUID());
	}

}
