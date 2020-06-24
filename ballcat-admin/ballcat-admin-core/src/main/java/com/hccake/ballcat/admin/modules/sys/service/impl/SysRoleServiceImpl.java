package com.hccake.ballcat.admin.modules.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.ballcat.admin.modules.sys.mapper.SysRoleMapper;
import com.hccake.ballcat.admin.modules.sys.mapper.SysRolePermissionMapper;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRolePermission;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysRoleQO;
import com.hccake.ballcat.admin.modules.sys.service.SysRoleService;
import com.hccake.ballcat.common.core.vo.SelectData;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

	private final SysRolePermissionMapper sysRolePermissionMapper;

	/**
	 * 查询系统角色列表
	 * @param page 分页对象
	 * @param qo 查询参数
	 * @return 分页对象
	 */
	@Override
	public IPage<SysRole> page(IPage<SysRole> page, SysRoleQO qo) {
		LambdaQueryWrapper<SysRole> wrapper = Wrappers.<SysRole>lambdaQuery()
				.like(StrUtil.isNotBlank(qo.getName()), SysRole::getName, qo.getName())
				.like(StrUtil.isNotBlank(qo.getCode()), SysRole::getCode, qo.getCode())
				.between(StrUtil.isNotBlank(qo.getStartTime()) && StrUtil.isNotBlank(qo.getEndTime()),
						SysRole::getCreateTime, qo.getStartTime(), qo.getEndTime());
		return baseMapper.selectPage(page, wrapper);
	}

	/**
	 * 通过角色ID，删除角色,并清空角色菜单缓存
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeRoleById(Integer id) {
		sysRolePermissionMapper
				.delete(Wrappers.<SysRolePermission>update().lambda().eq(SysRolePermission::getRoleId, id));
		return this.removeById(id);
	}

	/**
	 * 角色的选择数据
	 * @return
	 */
	@Override
	public List<SelectData> getSelectData() {
		return baseMapper.getSelectData();
	}

}
