package com.hccake.ballcat.codegen.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.codegen.mapper.TemplatePropertyMapper;
import com.hccake.ballcat.codegen.model.converter.TemplatePropertyConverter;
import com.hccake.ballcat.codegen.model.entity.TemplateProperty;
import com.hccake.ballcat.codegen.model.qo.TemplatePropertyQO;
import com.hccake.ballcat.codegen.model.vo.TemplatePropertyVO;
import com.hccake.ballcat.codegen.service.TemplatePropertyService;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.domain.PageResult;
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
	 * 获取模板组的所有配置
	 * @param templateGroupId 模板组ID
	 * @return List<TemplatePropertyVO> 配置列表
	 */
	@Override
	public List<TemplatePropertyVO> list(Integer templateGroupId) {
		List<TemplateProperty> templateProperties = baseMapper
				.selectList(Wrappers.<TemplateProperty>lambdaQuery().eq(TemplateProperty::getGroupId, templateGroupId));
		return templateProperties.stream().map(TemplatePropertyConverter.INSTANCE::poToVo).collect(Collectors.toList());
	}

	/**
	 * 复制模板属性配置
	 * @param resourceId 原模板组ID
	 * @param groupId 模板模板组ID
	 */
	@Override
	public void copy(Integer resourceId, Integer groupId) {

		List<TemplateProperty> templateProperties = baseMapper
				.selectList(Wrappers.<TemplateProperty>lambdaQuery().eq(TemplateProperty::getGroupId, resourceId));

		if (CollectionUtil.isNotEmpty(templateProperties)) {
			List<TemplateProperty> list = templateProperties.stream().peek(x -> {
				x.setId(null);
				x.setCreateTime(null);
				x.setUpdateTime(null);
				x.setGroupId(groupId);
			}).collect(Collectors.toList());
			this.saveBatch(list);
		}
	}

}
