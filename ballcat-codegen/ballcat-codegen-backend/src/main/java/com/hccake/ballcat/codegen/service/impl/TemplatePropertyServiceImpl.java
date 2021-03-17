package com.hccake.ballcat.codegen.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.codegen.mapper.TemplatePropertyMapper;
import com.hccake.ballcat.codegen.model.entity.TemplateProperty;
import com.hccake.ballcat.codegen.model.qo.TemplatePropertyQO;
import com.hccake.ballcat.codegen.model.vo.TemplatePropertyVO;
import com.hccake.ballcat.codegen.service.TemplatePropertyService;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 模板属性配置
 *
 * @author hccake
 * @date 2020-06-22 15:46:39
 */
@Service
public class TemplatePropertyServiceImpl extends ExtendServiceImpl<TemplatePropertyMapper, TemplateProperty>
		implements TemplatePropertyService {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return 分页数据
	 */
	@Override
	public PageResult<TemplatePropertyVO> queryPage(PageParam pageParam, TemplatePropertyQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	/**
	 * 根据模板组ID获取模板组的所有配置
	 * @param templateGroupId 模板组ID
	 * @return List<TemplateProperty> 配置列表
	 */
	@Override
	public List<TemplateProperty> listByTemplateGroupId(Integer templateGroupId) {
		return baseMapper.listByTemplateGroupId(templateGroupId);
	}

	/**
	 * 复制模板属性配置
	 * @param resourceGroupId 原模板组ID
	 * @param targetGroupId 模板模板组ID
	 */
	@Override
	public void copy(Integer resourceGroupId, Integer targetGroupId) {
		List<TemplateProperty> templateProperties = baseMapper.listByTemplateGroupId(resourceGroupId);
		if (CollectionUtil.isNotEmpty(templateProperties)) {
			List<TemplateProperty> list = templateProperties.stream().peek(x -> {
				x.setId(null);
				x.setCreateTime(null);
				x.setUpdateTime(null);
				x.setGroupId(targetGroupId);
			}).collect(Collectors.toList());
			baseMapper.insertBatchSomeColumn(list);
		}
	}

	/**
	 * 根据模板组ID 删除模板属性
	 * @param groupId 模板组ID
	 */
	@Override
	public void removeByGroupId(Integer groupId) {
		baseMapper.removeByGroupId(groupId);
	}

}
