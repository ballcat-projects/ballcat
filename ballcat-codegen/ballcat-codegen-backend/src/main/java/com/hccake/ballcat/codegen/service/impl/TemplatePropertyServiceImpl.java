package com.hccake.ballcat.codegen.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.ballcat.codegen.mapper.TemplatePropertyMapper;
import com.hccake.ballcat.codegen.model.converter.TemplatePropertyConverter;
import com.hccake.ballcat.codegen.model.entity.TemplateProperty;
import com.hccake.ballcat.codegen.model.qo.TemplatePropertyQO;
import com.hccake.ballcat.codegen.model.vo.TemplatePropertyVO;
import com.hccake.ballcat.codegen.service.TemplatePropertyService;
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
public class TemplatePropertyServiceImpl extends ServiceImpl<TemplatePropertyMapper, TemplateProperty>
		implements TemplatePropertyService {

	private final static String TABLE_ALIAS_PREFIX = "tp.";

	/**
	 * 根据QueryObeject查询分页数据
	 * @param page 分页参数
	 * @param qo 查询参数对象
	 * @return 分页数据
	 */
	@Override
	public IPage<TemplatePropertyVO> selectPageVo(IPage<?> page, TemplatePropertyQO qo) {
		QueryWrapper<TemplateProperty> wrapper = Wrappers.<TemplateProperty>query()
				.eq(TABLE_ALIAS_PREFIX + "group_id", qo.getGroupId())
				.eq(ObjectUtil.isNotNull(qo.getId()), TABLE_ALIAS_PREFIX + "id", qo.getId());
		return baseMapper.selectPageVo(page, wrapper);
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
