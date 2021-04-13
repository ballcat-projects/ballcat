package com.hccake.ballcat.admin.modules.system.service;

import com.hccake.ballcat.admin.modules.system.model.entity.LovSearch;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.List;

/**
 * @author lingting 2020-08-10 17:20
 */
public interface LovSearchService extends ExtendService<LovSearch> {

	/**
	 * 根据 lov keyword 查询对应 search
	 * @param keyword lov标识
	 * @return List<LovSearch>
	 */
	List<LovSearch> listByKeyword(String keyword);

	/**
	 * 根据 lov keyword 删除对应 search
	 * @param keyword lov标识
	 * @return 是否删除成功
	 */
	boolean removeByKeyword(String keyword);

}
