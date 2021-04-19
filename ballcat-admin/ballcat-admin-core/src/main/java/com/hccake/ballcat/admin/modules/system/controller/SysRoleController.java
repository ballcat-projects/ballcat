package com.hccake.ballcat.admin.modules.system.controller;

import com.hccake.ballcat.admin.constants.SysRoleConst;
import com.hccake.ballcat.admin.modules.system.converter.SysRoleConverter;
import com.hccake.ballcat.admin.modules.system.model.dto.SysRoleUpdateDTO;
import com.hccake.ballcat.admin.modules.system.model.entity.SysMenu;
import com.hccake.ballcat.admin.modules.system.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.system.model.qo.RoleBindUserQO;
import com.hccake.ballcat.admin.modules.system.model.qo.SysRoleQO;
import com.hccake.ballcat.admin.modules.system.model.vo.RoleBindUserVO;
import com.hccake.ballcat.admin.modules.system.model.vo.SysRolePageVO;
import com.hccake.ballcat.admin.modules.system.service.SysMenuService;
import com.hccake.ballcat.admin.modules.system.service.SysRoleMenuService;
import com.hccake.ballcat.admin.modules.system.service.SysRoleService;
import com.hccake.ballcat.admin.modules.system.service.SysUserRoleService;
import com.hccake.ballcat.commom.log.operation.annotation.CreateOperationLogging;
import com.hccake.ballcat.commom.log.operation.annotation.DeleteOperationLogging;
import com.hccake.ballcat.commom.log.operation.annotation.UpdateOperationLogging;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.domain.SelectData;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.model.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hccake
 */
@RestController
@RequestMapping("/system/role")
@Api(value = "system-role", tags = "角色管理模块")
@RequiredArgsConstructor
public class SysRoleController {

	private final SysRoleService sysRoleService;

	private final SysMenuService sysMenuService;

	private final SysUserRoleService sysUserRoleService;

	private final SysRoleMenuService sysRoleMenuService;

	/**
	 * 分页查询角色信息
	 * @param pageParam 分页参数
	 * @return PageResult 分页结果
	 */
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('system:role:read')")
	public R<PageResult<SysRolePageVO>> getRolePage(PageParam pageParam, SysRoleQO sysRoleQo) {
		return R.ok(sysRoleService.queryPage(pageParam, sysRoleQo));
	}

	/**
	 * 通过ID查询角色信息
	 * @param id ID
	 * @return 角色信息
	 */
	@GetMapping("/{id}")
	@PreAuthorize("@per.hasPermission('system:role:read')")
	public R<SysRole> getById(@PathVariable Integer id) {
		return R.ok(sysRoleService.getById(id));
	}

	/**
	 * 新增系统角色表
	 * @param sysRole 系统角色表
	 * @return R
	 */
	@ApiOperation(value = "新增系统角色", notes = "新增系统角色")
	@CreateOperationLogging(msg = "新增系统角色")
	@PostMapping
	@PreAuthorize("@per.hasPermission('system:role:add')")
	public R<Boolean> save(@Valid @RequestBody SysRole sysRole) {
		return R.ok(sysRoleService.save(sysRole));
	}

	/**
	 * 修改角色
	 * @param roleUpdateDTO 角色修改DTO
	 * @return success/false
	 */
	@ApiOperation(value = "修改系统角色", notes = "修改系统角色")
	@UpdateOperationLogging(msg = "修改系统角色")
	@PutMapping
	@PreAuthorize("@per.hasPermission('system:role:edit')")
	public R<Boolean> update(@Valid @RequestBody SysRoleUpdateDTO roleUpdateDTO) {
		SysRole sysRole = SysRoleConverter.INSTANCE.dtoToPo(roleUpdateDTO);
		return R.ok(sysRoleService.updateById(sysRole));
	}

	/**
	 * 删除角色
	 * @param id id
	 * @return 结果信息
	 */
	@DeleteMapping("/{id}")
	@ApiOperation(value = "通过id删除系统角色", notes = "通过id删除系统角色")
	@DeleteOperationLogging(msg = "通过id删除系统角色")
	@PreAuthorize("@per.hasPermission('system:role:del')")
	public R<Boolean> removeById(@PathVariable Integer id) {
		SysRole oldRole = sysRoleService.getById(id);
		if (SysRoleConst.Type.SYSTEM.getValue().equals(oldRole.getType())) {
			return R.failed(BaseResultCode.LOGIC_CHECK_ERROR, "系统角色不允许被删除!");
		}
		return R.ok(sysRoleService.removeById(id));
	}

	/**
	 * 获取角色列表
	 * @return 角色列表
	 */
	@GetMapping("/list")
	public R<List<SysRole>> listRoles() {
		return R.ok(sysRoleService.list());
	}

	/**
	 * 更新角色权限
	 * @param roleCode 角色Code
	 * @param permissionIds 权限ID数组
	 * @return success、false
	 */
	@PutMapping("/permission/code/{roleCode}")
	@ApiOperation(value = "更新角色权限", notes = "更新角色权限")
	@UpdateOperationLogging(msg = "更新角色权限")
	@PreAuthorize("@per.hasPermission('system:role:grant')")
	public R<Boolean> savePermissionIds(@PathVariable String roleCode, @RequestBody Integer[] permissionIds) {
		return R.ok(sysRoleMenuService.saveRoleMenus(roleCode, permissionIds));
	}

	/**
	 * 返回角色的菜单集合
	 * @param roleCode 角色ID
	 * @return 属性集合
	 */
	@GetMapping("/permission/code/{roleCode}")
	public R<List<Integer>> getPermissionIds(@PathVariable String roleCode) {
		List<SysMenu> sysMenus = sysMenuService.listByRoleCode(roleCode);
		List<Integer> menuIds = sysMenus.stream().map(SysMenu::getId).collect(Collectors.toList());
		return R.ok(menuIds);
	}

	/**
	 * 获取角色列表
	 * @return 角色列表
	 */
	@GetMapping("/select")
	public R<List<SelectData<?>>> listSelectData() {
		return R.ok(sysRoleService.listSelectData());
	}

	/**
	 * 分页查询已授权指定角色的用户列表
	 * @param roleBindUserQO 角色绑定用户的查询条件
	 * @return R
	 */
	@GetMapping("/user/page")
	@ApiOperation(value = "查看已授权指定角色的用户列表", notes = "查看已授权指定角色的用户列表")
	@PreAuthorize("@per.hasPermission('system:role:grant')")
	public R<PageResult<RoleBindUserVO>> queryUserPageByRoleCode(PageParam pageParam,
			@Valid RoleBindUserQO roleBindUserQO) {
		return R.ok(sysUserRoleService.queryUserPageByRoleCode(pageParam, roleBindUserQO));
	}

	/**
	 * 解绑与用户绑定关系
	 * @return R
	 */
	@DeleteMapping("/user")
	@ApiOperation(value = "解绑与用户绑定关系", notes = "解绑与用户绑定关系")
	@PreAuthorize("@per.hasPermission('system:role:grant')")
	public R<Boolean> unbindRoleUser(@RequestParam Integer userId, @RequestParam String roleCode) {
		return R.ok(sysUserRoleService.unbindRoleUser(userId, roleCode));
	}

}
