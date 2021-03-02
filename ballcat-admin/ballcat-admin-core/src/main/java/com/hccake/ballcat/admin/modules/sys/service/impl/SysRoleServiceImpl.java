package com.hccake.ballcat.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.admin.modules.sys.mapper.SysRoleMapper;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysRoleQO;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysRoleVO;
import com.hccake.ballcat.admin.modules.sys.service.SysRolePermissionService;
import com.hccake.ballcat.admin.modules.sys.service.SysRoleService;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.domain.SelectData;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ballcat
 * @since 2017-10-29
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ExtendServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

	private final SysRolePermissionService sysRolePermissionService;

	/**
	 * 查询系统角色列表
	 * @param pageParam 分页对象
	 * @param qo 查询参数
	 * @return 分页对象
	 */
	@Override
	public PageResult<SysRoleVO> queryPage(PageParam pageParam, SysRoleQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	/**
	 * 通过角色ID，删除角色,并清空角色菜单缓存
	 * @param id 角色ID
	 * @return boolean
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		SysRole role = getById(id);
		sysRolePermissionService.deleteByRoleCode(role.getCode());
		return SqlHelper.retBool(baseMapper.deleteById(id));
	}

	/**
	 * 角色的选择数据
	 * @return List<SelectData<?>>
	 */
	@Override
	public List<SelectData<?>> listSelectData() {
		return baseMapper.listSelectData();
	}

}
