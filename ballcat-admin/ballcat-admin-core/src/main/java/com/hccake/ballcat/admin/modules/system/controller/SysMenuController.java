package com.hccake.ballcat.admin.modules.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.admin.constants.SysPermissionConst;
import com.hccake.ballcat.admin.modules.system.converter.SysMenuConverter;
import com.hccake.ballcat.admin.modules.system.model.entity.SysMenu;
import com.hccake.ballcat.admin.modules.system.model.qo.SysMenuQO;
import com.hccake.ballcat.admin.modules.system.model.vo.SysMenuRouterVO;
import com.hccake.ballcat.admin.modules.system.service.SysMenuService;
import com.hccake.ballcat.admin.oauth.SysUserDetails;
import com.hccake.ballcat.admin.oauth.domain.UserResources;
import com.hccake.ballcat.admin.oauth.util.SecurityUtils;
import com.hccake.ballcat.commom.log.operation.annotation.CreateOperationLogging;
import com.hccake.ballcat.commom.log.operation.annotation.DeleteOperationLogging;
import com.hccake.ballcat.commom.log.operation.annotation.UpdateOperationLogging;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.model.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单权限
 *
 * @author hccake 2021-04-06 17:59:51
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/menu")
@Api(value = "system-menu", tags = "菜单权限管理")
public class SysMenuController {

	private final SysMenuService sysMenuService;

	/**
	 * 返回当前用户的路由集合
	 * @return 当前用户的路由
	 */
	@ApiOperation(value = "动态路由", notes = "动态路由")
	@GetMapping("/router")
	public R<List<SysMenuRouterVO>> getUserPermission() {

		// 获取角色Code
		SysUserDetails sysUserDetails = SecurityUtils.getSysUserDetails();
		UserResources userResources = sysUserDetails.getUserResources();
		Collection<String> roleCodes = userResources.getRoles();
		if (CollectionUtil.isEmpty(roleCodes)) {
			return R.ok(new ArrayList<>());
		}

		// 获取符合条件的权限
		Set<SysMenu> all = new HashSet<>();
		roleCodes.forEach(roleCode -> all.addAll(sysMenuService.listByRoleCode(roleCode)));

		// 筛选出菜单
		List<SysMenuRouterVO> menuVOList = all.stream()
				.filter(menuVo -> SysPermissionConst.Type.BUTTON.getValue() != menuVo.getType())
				.sorted(Comparator.comparingInt(SysMenu::getSort)).map(SysMenuConverter.INSTANCE::poToRouterVo)
				.collect(Collectors.toList());

		return R.ok(menuVOList);
	}

	/**
	 * 查询菜单列表
	 * @param sysMenuQO 菜单权限查询对象
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "查询菜单列表", notes = "查询菜单列表")
	@GetMapping("/list")
	@PreAuthorize("@per.hasPermission('system:menu:read')")
	public R<List<SysMenu>> getSysMenuPage(SysMenuQO sysMenuQO) {
		return R.ok(sysMenuService.listOrderBySort(sysMenuQO));
	}

	/**
	 * 新增菜单权限
	 * @param sysMenu 菜单权限
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "新增菜单权限", notes = "新增菜单权限")
	@CreateOperationLogging(msg = "新增菜单权限")
	@PostMapping
	@PreAuthorize("@per.hasPermission('system:menu:add')")
	public R<String> save(@RequestBody SysMenu sysMenu) {
		return sysMenuService.save(sysMenu) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增菜单权限失败");
	}

	/**
	 * 修改菜单权限
	 * @param sysMenu 菜单权限
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "修改菜单权限", notes = "修改菜单权限")
	@UpdateOperationLogging(msg = "修改菜单权限")
	@PutMapping
	@PreAuthorize("@per.hasPermission('system:menu:edit')")
	public R<String> updateById(@RequestBody SysMenu sysMenu) {
		return sysMenuService.updateById(sysMenu) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改菜单权限失败");
	}

	/**
	 * 通过id删除菜单权限
	 * @param id id
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "通过id删除菜单权限", notes = "通过id删除菜单权限")
	@DeleteOperationLogging(msg = "通过id删除菜单权限")
	@DeleteMapping("/{id}")
	@PreAuthorize("@per.hasPermission('system:menu:del')")
	public R<String> removeById(@PathVariable Integer id) {
		return sysMenuService.removeById(id) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除菜单权限失败");
	}

}