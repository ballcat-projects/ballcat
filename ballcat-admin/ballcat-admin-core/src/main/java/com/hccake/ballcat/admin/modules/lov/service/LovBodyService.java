package com.hccake.ballcat.admin.modules.lov.service;

import com.hccake.ballcat.admin.modules.lov.model.entity.LovBody;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.List;

/**
 * @author lingting 2020-08-10 17:20
 */
public interface LovBodyService extends ExtendService<LovBody> {

	/**
	 * 根据 lov keyword 查询对应 body
	 * @param keyword lov标识
	 * @return List<LovBody>
	 */
	List<LovBody> listByKeyword(String keyword);

	/**
	 * 根据 lov keyword 删除对应 body
	 * @param keyword lov标识
	 * @return 是否删除成功
	 */
	boolean removeByKeyword(String keyword);

}
