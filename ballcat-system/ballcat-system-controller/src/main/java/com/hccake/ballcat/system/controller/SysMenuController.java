package com.hccake.ballcat.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.common.log.operation.annotation.CreateOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.DeleteOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.UpdateOperationLogging;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.common.security.constant.TokenAttributeNameConstants;
import com.hccake.ballcat.common.security.userdetails.User;
import com.hccake.ballcat.common.security.util.SecurityUtils;
import com.hccake.ballcat.system.constant.SysPermissionConst;
import com.hccake.ballcat.system.converter.SysMenuConverter;
import com.hccake.ballcat.system.model.dto.SysMenuCreateDTO;
import com.hccake.ballcat.system.model.dto.SysMenuUpdateDTO;
import com.hccake.ballcat.system.model.entity.SysMenu;
import com.hccake.ballcat.system.model.qo.SysMenuQO;
import com.hccake.ballcat.system.model.vo.SysMenuGrantVO;
import com.hccake.ballcat.system.model.vo.SysMenuPageVO;
import com.hccake.ballcat.system.model.vo.SysMenuRouterVO;
import com.hccake.ballcat.system.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
		User user = SecurityUtils.getUser();
		Map<String, Object> attributes = user.getAttributes();

		Object rolesObject = attributes.get(TokenAttributeNameConstants.ROLES);
		if (!(rolesObject instanceof Collection)) {
			return R.ok(new ArrayList<>());
		}

		@SuppressWarnings("unchecked")
		Collection<String> roleCodes = (Collection<String>) rolesObject;
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
	public R<List<SysMenuPageVO>> getSysMenuPage(SysMenuQO sysMenuQO) {
		List<SysMenu> sysMenus = sysMenuService.listOrderBySort(sysMenuQO);
		if (CollectionUtil.isEmpty(sysMenus)) {
			R.ok(new ArrayList<>());
		}
		List<SysMenuPageVO> voList = sysMenus.stream().map(SysMenuConverter.INSTANCE::poToPageVo)
				.collect(Collectors.toList());
		return R.ok(voList);
	}

	/**
	 * 查询授权菜单列表
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "查询授权菜单列表", notes = "查询授权菜单列表")
	@GetMapping("/grant-list")
	@PreAuthorize("@per.hasPermission('system:menu:read')")
	public R<List<SysMenuGrantVO>> getSysMenuGrantList() {
		List<SysMenu> sysMenus = sysMenuService.list();
		if (CollectionUtil.isEmpty(sysMenus)) {
			R.ok(new ArrayList<>());
		}
		List<SysMenuGrantVO> voList = sysMenus.stream().map(SysMenuConverter.INSTANCE::poToGrantVo)
				.collect(Collectors.toList());
		return R.ok(voList);
	}

	/**
	 * 新增菜单权限
	 * @param sysMenuCreateDTO 菜单权限
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "新增菜单权限", notes = "新增菜单权限")
	@CreateOperationLogging(msg = "新增菜单权限")
	@PostMapping
	@PreAuthorize("@per.hasPermission('system:menu:add')")
	public R<String> save(@Valid @RequestBody SysMenuCreateDTO sysMenuCreateDTO) {
		return sysMenuService.create(sysMenuCreateDTO) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增菜单权限失败");
	}

	/**
	 * 修改菜单权限
	 * @param sysMenuUpdateDTO 菜单权限修改DTO
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "修改菜单权限", notes = "修改菜单权限")
	@UpdateOperationLogging(msg = "修改菜单权限")
	@PutMapping
	@PreAuthorize("@per.hasPermission('system:menu:edit')")
	public R<String> updateById(@RequestBody SysMenuUpdateDTO sysMenuUpdateDTO) {
		sysMenuService.update(sysMenuUpdateDTO);
		return R.ok();
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
	public R<String> removeById(@PathVariable("id") Integer id) {
		return sysMenuService.removeById(id) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除菜单权限失败");
	}

}