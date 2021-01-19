package com.hccake.ballcat.codegen.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.codegen.model.entity.TemplateInfo;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;

import java.util.List;

;

/**
 * 模板信息
 *
 * @author hccake
 * @date 2020-06-19 18:09:08
 */
public interface TemplateInfoMapper extends ExtendMapper<TemplateInfo> {

	/**
	 * List template info list.
	 * @param templateGroupId the template group id
	 * @return List<TemplateInfo> the list
	 */
	default List<TemplateInfo> listByTemplateGroupId(Integer templateGroupId) {
		return this.selectList(Wrappers.<TemplateInfo>lambdaQuery().eq(TemplateInfo::getGroupId, templateGroupId));
	}

}