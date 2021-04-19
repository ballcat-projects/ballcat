package com.hccake.ballcat.admin.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.admin.modules.system.converter.SysOrganizationConverter;
import com.hccake.ballcat.admin.modules.system.mapper.SysOrganizationMapper;
import com.hccake.ballcat.admin.modules.system.model.dto.SysOrganizationDTO;
import com.hccake.ballcat.admin.modules.system.model.entity.SysOrganization;
import com.hccake.ballcat.admin.modules.system.model.qo.SysOrganizationQO;
import com.hccake.ballcat.admin.modules.system.model.vo.SysOrganizationTree;
import com.hccake.ballcat.admin.modules.system.service.SysOrganizationService;
import com.hccake.ballcat.common.core.constant.GlobalConstants;
import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.util.TreeUtils;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 组织架构
 *
 * @author hccake 2020-09-23 12:09:43
 */
@Service
public class SysOrganizationServiceImpl extends ExtendServiceImpl<SysOrganizationMapper, SysOrganization>
		implements SysOrganizationService {

	/**
	 * 返回组织架构的树形结构
	 * @param sysOrganizationQO 组织机构查询条件
	 * @return OrganizationTree
	 */
	@Override
	public List<SysOrganizationTree> listTree(SysOrganizationQO sysOrganizationQO) {
		List<SysOrganization> list = baseMapper.selectList(sysOrganizationQO);
		return TreeUtils.buildTree(list, 0, SysOrganizationConverter.INSTANCE::poToTree);
	}

	/**
	 * 创建一个新的组织机构
	 * @param sysOrganizationDTO 组织机构DTO
	 * @return boolean 创建成功/失败
	 */
	@Override
	public boolean create(SysOrganizationDTO sysOrganizationDTO) {
		sysOrganizationDTO.setId(null);
		SysOrganization sysOrganization = SysOrganizationConverter.INSTANCE.dtoToPo(sysOrganizationDTO);

		// 如果父级为根节点则直接设置深度和层级，否则根据父节点数据动态设置
		Integer parentId = sysOrganizationDTO.getParentId();
		// 填充层级和深度
		fillDepthAndHierarchy(sysOrganization, parentId);

		return SqlHelper.retBool(baseMapper.insert(sysOrganization));
	}

	/**
	 * 更新一个已有的组织机构
	 * @param sysOrganizationDTO 组织机构DTO
	 * @return boolean 更新成功/失败
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean update(SysOrganizationDTO sysOrganizationDTO) {
		// TODO 防止并发问题
		SysOrganization sysOrganization = SysOrganizationConverter.INSTANCE.dtoToPo(sysOrganizationDTO);
		Integer organizationId = sysOrganization.getId();
		SysOrganization originSysOrganization = baseMapper.selectById(organizationId);

		// 如果没有移动父节点，则直接更新
		Integer targetParentId = sysOrganizationDTO.getParentId();
		if (originSysOrganization.getParentId().equals(targetParentId)) {
			return SqlHelper.retBool(baseMapper.updateById(sysOrganization));
		}

		// 移动了父节点，先判断不是选择自己作为父节点
		Assert.isFalse(targetParentId.equals(organizationId), "父节点不能是自己！");
		// 再判断是否是自己的子节点，根节点跳过判断
		if (!GlobalConstants.TREE_ROOT_ID.equals(targetParentId)) {
			SysOrganization targetParentOrganization = baseMapper.selectById(targetParentId);
			String[] targetParentHierarchy = targetParentOrganization.getHierarchy().split("-");
			if (ArrayUtil.contains(targetParentHierarchy, String.valueOf(organizationId))) {
				throw new BusinessException(BaseResultCode.LOGIC_CHECK_ERROR.getCode(), "父节点不能是自己的子节点！");
			}
		}

		// 填充目标层级和深度
		fillDepthAndHierarchy(sysOrganization, targetParentId);
		// 原来的层级和深度
		String originHierarchy = originSysOrganization.getHierarchy();
		int originDepth = originSysOrganization.getDepth();
		// 计算出更换父节点后的层级和深度
		int depthDiff = originDepth - sysOrganization.getDepth();
		// 更新其子节点的数据
		baseMapper.followMoveChildNode(originHierarchy, sysOrganization.getHierarchy(), depthDiff);

		return SqlHelper.retBool(baseMapper.updateById(sysOrganization));
	}

	/**
	 * 根据组织ID 查询除该组织下的所有儿子组织
	 * @param organizationId 组织机构ID
	 * @return List<SysOrganization> 该组织的儿子组织
	 */
	@Override
	public List<SysOrganization> listSubOrganization(Integer organizationId) {
		return baseMapper.listSubOrganization(organizationId);
	}

	/**
	 * 根据组织ID 查询除该组织下的所有孩子（子孙）组织
	 * @param organizationId 组织机构ID
	 * @return List<SysOrganization> 该组织的孩子组织
	 */
	@Override
	public List<SysOrganization> listChildOrganization(Integer organizationId) {
		return baseMapper.listChildOrganization(organizationId);
	}

	/**
	 * 根据组织ID 删除组织机构
	 * @param organizationId 组织机构ID
	 * @return 删除成功: true
	 */
	@Override
	public boolean removeById(Serializable organizationId) {
		List<SysOrganization> children = baseMapper.listChildOrganization((Integer) organizationId);
		if (CollectionUtil.isNotEmpty(children)) {
			throw new BusinessException(BaseResultCode.LOGIC_CHECK_ERROR.getCode(), "该组织机构拥有下级组织，不能删除！");
		}
		return SqlHelper.retBool(baseMapper.deleteById(organizationId));
	}

	/**
	 * 根据父级ID填充当前组织机构实体的深度和层级
	 * @param sysOrganization 组织机构实体
	 * @param parentId 父级ID
	 */
	private void fillDepthAndHierarchy(SysOrganization sysOrganization, Integer parentId) {
		if (GlobalConstants.TREE_ROOT_ID.equals(parentId)) {
			sysOrganization.setDepth(1);
			sysOrganization.setHierarchy(GlobalConstants.TREE_ROOT_ID.toString());
		}
		else {
			SysOrganization parentSysOrganization = baseMapper.selectById(parentId);
			Assert.notNull(parentSysOrganization, "不存在的父级组织机构！");

			sysOrganization.setDepth(parentSysOrganization.getDepth() + 1);
			sysOrganization.setHierarchy(parentSysOrganization.getHierarchy() + "-" + parentSysOrganization.getId());
		}
	}

}
