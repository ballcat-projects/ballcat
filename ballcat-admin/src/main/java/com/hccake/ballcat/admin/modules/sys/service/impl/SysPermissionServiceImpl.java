package com.hccake.ballcat.admin.modules.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.ballcat.admin.modules.sys.mapper.SysPermissionMapper;
import com.hccake.ballcat.admin.modules.sys.mapper.SysRolePermissionMapper;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysPermission;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRolePermission;
import com.hccake.ballcat.admin.modules.sys.model.vo.PermissionVO;
import com.hccake.ballcat.admin.modules.sys.service.SysPermissionService;
import com.hccake.ballcat.common.core.exception.BallCatException;
import com.hccake.ballcat.common.core.result.ResultStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author
 * @since 2017-10-29
 */
@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {
	private final SysRolePermissionMapper sysRolePermissionMapper;

	@Override
	public List<PermissionVO> findPermissionVOByRoleId(Integer roleId) {
		return baseMapper.listPermissionVOsByRoleId(roleId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removePermissionById(Integer id) {
		// 查询父节点为当前节点的节点
		List<SysPermission> permissionList = this.list(Wrappers.<SysPermission>query()
				.lambda().eq(SysPermission::getParentId, id));
		if (CollUtil.isNotEmpty(permissionList)) {
			throw new BallCatException(ResultStatus.LOGIC_CHECK_ERROR.getCode(), "菜单含有下级不能删除");
		}

		sysRolePermissionMapper
				.delete(Wrappers.<SysRolePermission>query()
						.lambda().eq(SysRolePermission::getPermissionId, id));

		//删除当前菜单及其子菜单
		return this.removeById(id);
	}

	@Override
	public Boolean updatePermissionById(SysPermission sysPermission) {
		return this.updateById(sysPermission);
	}
}
