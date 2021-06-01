package com.hccake.ballcat.system.service;

import com.hccake.ballcat.system.model.entity.SysLovSearch;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.List;

/**
 * @author lingting 2020-08-10 17:20
 */
public interface SysLovSearchService extends ExtendService<SysLovSearch> {

	/**
	 * 根据 lov keyword 查询对应 search
	 * @param keyword lov标识
	 * @return List<LovSearch>
	 */
	List<SysLovSearch> listByKeyword(String keyword);

	/**
	 * 根据 lov keyword 删除对应 search
	 * @param keyword lov标识
	 * @return 是否删除成功
	 */
	boolean removeByKeyword(String keyword);

}
