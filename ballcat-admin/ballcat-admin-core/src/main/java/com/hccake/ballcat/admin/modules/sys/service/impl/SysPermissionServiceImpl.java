package com.hccake.ballcat.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.admin.modules.sys.mapper.SysPermissionMapper;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysPermission;
import com.hccake.ballcat.admin.modules.sys.model.vo.PermissionVO;
import com.hccake.ballcat.admin.modules.sys.service.SysPermissionService;
import com.hccake.ballcat.admin.modules.sys.service.SysRolePermissionService;
import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.ballcat.common.core.result.BaseResultCode;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author hccake
 * @since 2017-10-29
 */
@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl extends ExtendServiceImpl<SysPermissionMapper, SysPermission>
		implements SysPermissionService {

	private final SysRolePermissionService sysRolePermissionService;

	@Override
	public List<PermissionVO> listVOByRoleCode(String roleCode) {
		return baseMapper.listVOByRoleCode(roleCode);
	}

	/**
	 * 查询权限集合，并按sort排序（升序）
	 * @return List<SysPermission>
	 */
	@Override
	public List<SysPermission> listOrderBySort() {
		return baseMapper.listOrderBySort();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		// 查询当前权限是否有子权限
		Integer subPermissionNum = baseMapper.countSubPermission(id);
		if (subPermissionNum != null && subPermissionNum > 0) {
			throw new BusinessException(BaseResultCode.LOGIC_CHECK_ERROR.getCode(), "菜单含有下级不能删除");
		}
		// 删除角色权限关联数据
		sysRolePermissionService.deleteByPermissionId(id);
		// 删除当前菜单及其子菜单
		return SqlHelper.retBool(baseMapper.deleteById(id));
	}

}
