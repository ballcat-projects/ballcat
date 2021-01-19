package com.hccake.ballcat.admin.modules.sys.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysPermission;
import com.hccake.ballcat.admin.modules.sys.model.vo.PermissionVO;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单权限表 Mapper 接口
 *
 * @author hccake
 */
public interface SysPermissionMapper extends ExtendMapper<SysPermission> {

	/**
	 * 查询指定权限的下级权限总数
	 * @param id 权限ID
	 * @return 下级权限总数
	 */
	default Integer countSubPermission(Serializable id) {
		return this.selectCount(Wrappers.<SysPermission>query().lambda().eq(SysPermission::getParentId, id));
	}

	/**
	 * 查询权限集合，并按sort排序（升序）
	 * @return List<SysPermission>
	 */
	default List<SysPermission> listOrderBySort() {
		return this.selectList(Wrappers.<SysPermission>lambdaQuery().orderByAsc(SysPermission::getSort));
	}

	/**
	 * 通过角色ID查询权限
	 * @param roleCode 角色ID
	 * @return 指定角色拥有的权限列表
	 */
	List<PermissionVO> listVOByRoleCode(String roleCode);

}
