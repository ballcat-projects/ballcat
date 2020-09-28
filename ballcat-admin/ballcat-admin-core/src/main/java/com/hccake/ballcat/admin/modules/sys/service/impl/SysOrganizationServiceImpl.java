package com.hccake.ballcat.admin.modules.sys.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.admin.modules.sys.mapper.SysOrganizationMapper;
import com.hccake.ballcat.admin.modules.sys.model.converter.SysOrganizationConverter;
import com.hccake.ballcat.admin.modules.sys.model.dto.SysOrganizationDTO;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysOrganization;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysOrganizationTree;
import com.hccake.ballcat.admin.modules.sys.service.SysOrganizationService;
import com.hccake.ballcat.common.core.constant.GlobalConstants;
import com.hccake.ballcat.common.core.util.TreeUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 组织架构
 *
 * @author hccake 2020-09-23 12:09:43
 */
@Service
public class SysOrganizationServiceImpl extends ServiceImpl<SysOrganizationMapper, SysOrganization>
		implements SysOrganizationService {

	private final static String TABLE_ALIAS_PREFIX = "o.";

	/**
	 * 返回组织架构的树形结构
	 * @return OrganizationTree
	 */
	@Override
	public List<SysOrganizationTree> tree() {
		List<SysOrganization> list = baseMapper.selectList(null);
		return TreeUtil.buildTree(list, 0, SysOrganizationConverter.INSTANCE::poToTree);
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
	public boolean update(SysOrganizationDTO sysOrganizationDTO) {

		SysOrganization sysOrganization = SysOrganizationConverter.INSTANCE.dtoToPo(sysOrganizationDTO);
		SysOrganization originSysOrganization = baseMapper.selectById(sysOrganization.getId());

		// 如果没有移动父节点，则直接更新
		Integer targetParentId = sysOrganizationDTO.getParentId();
		if (originSysOrganization.getParentId().equals(targetParentId)) {
			return SqlHelper.retBool(baseMapper.updateById(sysOrganization));
		}

		// 移动了父节点，先判断不是选择自己作为父节点
		Assert.isFalse(targetParentId.equals(sysOrganization.getId()), "父节点不能是自己！");
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
	public List<SysOrganization> selectSubOrganization(Integer organizationId) {
		return baseMapper
				.selectList(Wrappers.<SysOrganization>lambdaQuery().eq(SysOrganization::getParentId, organizationId));
	}

	/**
	 * 根据组织ID 查询除该组织下的所有孩子（子孙）组织
	 * @param organizationId 组织机构ID
	 * @return List<SysOrganization> 该组织的孩子组织
	 */
	@Override
	public List<SysOrganization> selectChildOrganization(Integer organizationId) {
		return baseMapper.selectChildOrganization(organizationId);
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
