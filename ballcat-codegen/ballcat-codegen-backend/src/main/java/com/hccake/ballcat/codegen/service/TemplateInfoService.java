package com.hccake.ballcat.codegen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hccake.ballcat.codegen.model.entity.TemplateInfo;

import java.util.List;

/**
 * 模板信息
 *
 * @author hccake
 * @date 2020 -06-18 18:32:51
 */
public interface TemplateInfoService extends IService<TemplateInfo> {

	/**
	 * List template info list.
	 * @param templateGroupId the template group id
	 * @return the list
	 */
	List<TemplateInfo> listTemplateInfo(Integer templateGroupId);

}
