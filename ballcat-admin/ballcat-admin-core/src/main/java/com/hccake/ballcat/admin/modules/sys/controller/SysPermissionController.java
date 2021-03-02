package com.hccake.ballcat.admin.modules.sys.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.admin.constants.SysPermissionConst;
import com.hccake.ballcat.admin.constants.UserResourceConstant;
import com.hccake.ballcat.admin.modules.sys.model.converter.SysPermissionConverter;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysPermission;
import com.hccake.ballcat.admin.modules.sys.model.vo.PermissionVO;
import com.hccake.ballcat.admin.modules.sys.model.vo.Router;
import com.hccake.ballcat.admin.modules.sys.service.SysPermissionService;
import com.hccake.ballcat.admin.oauth.SysUserDetails;
import com.hccake.ballcat.admin.oauth.util.SecurityUtils;
import com.hccake.ballcat.commom.log.operation.annotation.CreateOperationLogging;
import com.hccake.ballcat.commom.log.operation.annotation.DeleteOperationLogging;
import com.hccake.ballcat.commom.log.operation.annotation.UpdateOperationLogging;
import com.hccake.ballcat.common.model.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hccake
 * @date 2019/09/17
 */
@RestController
@RequestMapping("/syspermission")
@Api(value = "syspermission", tags = "权限管理模块")
@RequiredArgsConstructor
public class SysPermissionController {

	private final SysPermissionService sysPermissionService;

	/**
	 * 返回当前用户的路由集合
	 * @return 当前用户的路由
	 */
	@ApiOperation(value = "动态路由", notes = "动态路由")
	@GetMapping("/router")
	public R<List<Router>> getUserPermission() {

		// 获取角色Code
		SysUserDetails sysUserDetails = SecurityUtils.getSysUserDetails();
		Map<String, Collection<?>> userResources = sysUserDetails.getUserResources();
		Collection<String> roleCodes = (Collection<String>) userResources.get(UserResourceConstant.RESOURCE_ROLE);
		if (CollectionUtil.isEmpty(roleCodes)) {
			return R.ok(new ArrayList<>());
		}

		// 获取符合条件的权限
		Set<PermissionVO> all = new HashSet<>();
		roleCodes.forEach(roleCode -> all.addAll(sysPermissionService.listVOByRoleCode(roleCode)));

		// 筛选出菜单
		List<Router> routerList = all.stream()
				.filter(menuVo -> SysPermissionConst.Type.MENU.getValue() == menuVo.getType()
						|| SysPermissionConst.Type.DIRECTORY.getValue() == menuVo.getType())
				.sorted(Comparator.comparingInt(PermissionVO::getSort)).map(SysPermissionConverter.INSTANCE::toRouter)
				.collect(Collectors.toList());

		return R.ok(routerList);
	}

	/**
	 * 所有的权限集合
	 */
	@GetMapping(value = "/list")
	@PreAuthorize("@per.hasPermission('sys:syspermission:read')")
	public R<List<SysPermission>> getTree() {
		return R.ok(sysPermissionService.listOrderBySort());
	}

	/**
	 * 通过ID查询权限的详细信息
	 * @param id 权限ID
	 * @return 权限详细信息
	 */
	@GetMapping("/{id}")
	@PreAuthorize("@per.hasPermission('sys:syspermission:read')")
	public R<SysPermission> getById(@PathVariable Integer id) {
		return R.ok(sysPermissionService.getById(id));
	}

	@ApiOperation(value = "新增权限", notes = "新增权限")
	@CreateOperationLogging(msg = "新增权限")
	@PostMapping
	@PreAuthorize("@per.hasPermission('sys:syspermission:add')")
	public R<Boolean> save(@Valid @RequestBody SysPermission sysMenu) {
		return R.ok(sysPermissionService.save(sysMenu));
	}

	/**
	 * 更新权限
	 * @param sysPermission 权限
	 * @return R
	 */
	@ApiOperation(value = "修改权限", notes = "修改权限")
	@UpdateOperationLogging(msg = "修改权限")
	@PutMapping
	@PreAuthorize("@per.hasPermission('sys:syspermission:edit')")
	public R<Boolean> update(@Valid @RequestBody SysPermission sysPermission) {
		return R.ok(sysPermissionService.updateById(sysPermission));
	}

	@ApiOperation(value = "通过id删除权限", notes = "通过id删除权限")
	@DeleteOperationLogging(msg = "通过id删除权限")
	@DeleteMapping("/{id}")
	@PreAuthorize("@per.hasPermission('sys:syspermission:del')")
	public R<Boolean> removeById(@PathVariable Integer id) {
		return R.ok(sysPermissionService.removeById(id));
	}

}
