package com.hccake.ballcat.admin.modules.sys.service;

import com.hccake.ballcat.admin.modules.sys.model.entity.SysDict;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysDictQO;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysDictVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.List;

/**
 * 字典表
 *
 * @author hccake
 * @date 2020-03-26 18:40:20
 */
public interface SysDictService extends ExtendService<SysDict> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<SysDictVO> 分页数据
	 */
	PageResult<SysDictVO> queryPage(PageParam pageParam, SysDictQO qo);

	/**
	 * 根据字典标识查询
	 * @param dictCode 字典标识
	 * @return 字典数据
	 */
	SysDict getByCode(String dictCode);

	/**
	 * 根据字典标识数组查询对应字典集合
	 * @param dictCodes 字典标识数组
	 * @return List<SysDict> 字典集合
	 */
	List<SysDict> listByCodes(String[] dictCodes);

	/**
	 * 更新字典HashCode
	 * @param dictCode 字典标识
	 * @return 更新状态 成功（true） or 失败 (false)
	 */
	boolean updateHashCode(String dictCode);

}
