package com.hccake.ballcat.codegen.service.impl;

import com.hccake.ballcat.codegen.mapper.TemplateInfoMapper;
import com.hccake.ballcat.codegen.model.dto.TemplateFileContentDTO;
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
	public List<TemplateInfo> listByTemplateGroupId(Integer templateGroupId) {
		return baseMapper.listByTemplateGroupId(templateGroupId);
	}

	/**
	 * 删除模板文件
	 * @param groupId 模板组ID
	 */
	@Override
	public void removeByGroupId(Integer groupId) {
		baseMapper.deleteByGroupId(groupId);
	}

	/**
	 * 更新模板文件内容主题
	 * @param templateFileContentDTO 模板文件内容DTO
	 * @return 成功：true
	 */
	@Override
	public boolean updateContent(TemplateFileContentDTO templateFileContentDTO) {
		TemplateInfo templateInfo = new TemplateInfo();
		templateInfo.setDirectoryEntryId(templateFileContentDTO.getDirectoryEntryId());
		templateInfo.setContent(templateFileContentDTO.getContent());
		return this.updateById(templateInfo);
	}

}
