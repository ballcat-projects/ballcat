package com.hccake.ballcat.codegen.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.codegen.mapper.TemplateInfoMapper;
import com.hccake.ballcat.codegen.model.entity.TemplateInfo;
import com.hccake.ballcat.codegen.service.TemplateInfoService;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 模板信息
 *
 * @author hccake
 * @date 2020-06-18 18:32:51
 */
@Service
public class TemplateInfoServiceImpl extends ExtendServiceImpl<TemplateInfoMapper, TemplateInfo>
		implements TemplateInfoService {

	/**
	 * List template info list.
	 * @param templateGroupId the template group id
	 * @return the list
	 */
	@Override
	public List<TemplateInfo> listTemplateInfo(Integer templateGroupId) {
		return baseMapper
				.selectList(Wrappers.<TemplateInfo>lambdaQuery().eq(TemplateInfo::getGroupId, templateGroupId));
	}

}
