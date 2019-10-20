package com.hccake.ballcat.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRolePermission;
import com.hccake.ballcat.admin.modules.sys.mapper.SysRoleMapper;
import com.hccake.ballcat.admin.modules.sys.mapper.SysRolePermissionMapper;
import com.hccake.ballcat.admin.modules.sys.service.SysRoleService;
import com.hccake.ballcat.common.core.vo.SelectData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author
 * @since 2017-10-29
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

	@Autowired
	private SysRolePermissionMapper sysRolePermissionMapper;

	/**
	 * 通过角色ID，删除角色,并清空角色菜单缓存
	 *
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeRoleById(Integer id) {
		sysRolePermissionMapper.delete(Wrappers
			.<SysRolePermission>update().lambda()
			.eq(SysRolePermission::getRoleId, id));
		return this.removeById(id);
	}

	/**
	 * 角色的选择数据
	 *
	 * @return
	 */
	@Override
	public List<SelectData> getSelectData() {
		return baseMapper.getSelectData();
	}
}
