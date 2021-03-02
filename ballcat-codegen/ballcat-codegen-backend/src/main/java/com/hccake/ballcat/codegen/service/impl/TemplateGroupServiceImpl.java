package com.hccake.ballcat.codegen.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.codegen.mapper.TemplateGroupMapper;
import com.hccake.ballcat.codegen.model.entity.TemplateGroup;
import com.hccake.ballcat.codegen.model.qo.TemplateGroupQO;
import com.hccake.ballcat.codegen.model.vo.TemplateGroupVO;
import com.hccake.ballcat.codegen.service.TemplateDirectoryEntryService;
import com.hccake.ballcat.codegen.service.TemplateGroupService;
import com.hccake.ballcat.codegen.service.TemplatePropertyService;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.domain.SelectData;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 模板组
 *
 * @author hccake
 * @date 2020-06-19 19:11:41
 */
@Service
@RequiredArgsConstructor
public class TemplateGroupServiceImpl extends ExtendServiceImpl<TemplateGroupMapper, TemplateGroup>
		implements TemplateGroupService {

	private final TemplateDirectoryEntryService templateDirectoryEntryService;

	private final TemplatePropertyService templatePropertyService;

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return 分页数据
	 */
	@Override
	public PageResult<TemplateGroupVO> queryPage(PageParam pageParam, TemplateGroupQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	/**
	 * 获取SelectData数据
	 * @return List<SelectData<?>>
	 */
	@Override
	public List<SelectData<?>> listSelectData() {
		return baseMapper.listSelectData();
	}

	/**
	 * 复制模板组
	 * @param resourceGroupId 原资源组id
	 * @param templateGroup 模板组
	 * @return boolean 复制成功: true
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean copy(Integer resourceGroupId, TemplateGroup templateGroup) {
		// 清空id
		templateGroup.setId(null);
		int insertFlag = baseMapper.insert(templateGroup);
		Assert.isTrue(SqlHelper.retBool(insertFlag), "复制模板组时，保存模板组失败：[{}]", templateGroup);
		// 获取落库成功后的自增ID
		Integer groupId = templateGroup.getId();
		// 复制模板目录文件
		templateDirectoryEntryService.copy(resourceGroupId, groupId);
		// 复制模板属性配置
		templatePropertyService.copy(resourceGroupId, groupId);

		return true;
	}

}