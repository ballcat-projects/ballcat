package com.hccake.ballcat.codegen.service;

import com.hccake.ballcat.codegen.model.entity.TemplateInfo;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.List;

/**
 * 模板信息
 *
 * @author hccake
 * @date 2020 -06-18 18:32:51
 */
public interface TemplateInfoService extends ExtendService<TemplateInfo> {

	/**
	 * List template info list.
	 * @param templateGroupId the template group id
	 * @return the list
	 */
	List<TemplateInfo> listByTemplateGroupId(Integer templateGroupId);

	/**
	 * 删除模板文件
	 *
	 * @param groupId 模板组ID
	 */
    void removeByGroupId(Integer groupId);
}
