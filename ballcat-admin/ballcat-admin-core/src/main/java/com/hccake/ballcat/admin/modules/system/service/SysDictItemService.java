package com.hccake.ballcat.admin.modules.system.service;

import com.hccake.ballcat.admin.modules.system.model.entity.SysDictItem;
import com.hccake.ballcat.admin.modules.system.model.vo.SysDictItemPageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.List;

/**
 * 字典项
 *
 * @author hccake
 * @date 2020-03-26 18:40:20
 */
public interface SysDictItemService extends ExtendService<SysDictItem> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param dictCode 查询参数对象
	 * @return 分页数据
	 */
	PageResult<SysDictItemPageVO> queryPage(PageParam pageParam, String dictCode);

	/**
	 * 根据Code查询对应字典项数据
	 * @param dictCode 字典标识
	 * @return 该字典对应的字典项集合
	 */
	List<SysDictItem> listByDictCode(String dictCode);

	/**
	 * 根据字典标识删除对应字典项
	 * @param dictCode 字典标识
	 * @return 是否删除成功
	 */
	boolean removeByDictCode(String dictCode);

}
