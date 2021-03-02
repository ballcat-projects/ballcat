package com.hccake.ballcat.admin.modules.sys.controller;

import com.hccake.ballcat.admin.constants.SysRoleConst;
import com.hccake.ballcat.admin.modules.sys.model.converter.SysRoleConverter;
import com.hccake.ballcat.admin.modules.sys.model.dto.SysRoleUpdateDTO;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysRoleQO;
import com.hccake.ballcat.admin.modules.sys.model.vo.PermissionVO;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysRoleVO;
import com.hccake.ballcat.admin.modules.sys.service.SysPermissionService;
import com.hccake.ballcat.admin.modules.sys.service.SysRolePermissionService;
import com.hccake.ballcat.admin.modules.sys.service.SysRoleService;
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
@RequestMapping("/sysrole")
@Api(value = "sysrole", tags = "角色管理模块")
@RequiredArgsConstructor
public class SysRoleController {

	private final SysRoleService sysRoleService;

	private final SysRolePermissionService sysRolePermissionService;

	private final SysPermissionService sysPermissionService;

	/**
	 * 分页查询角色信息
	 * @param pageParam 分页参数
	 * @return PageResult 分页结果
	 */
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('sys:sysrole:read')")
	public R<PageResult<SysRoleVO>> getRolePage(PageParam pageParam, SysRoleQO sysRoleQo) {
		return R.ok(sysRoleService.queryPage(pageParam, sysRoleQo));
	}

	/**
	 * 通过ID查询角色信息
	 * @param id ID
	 * @return 角色信息
	 */
	@GetMapping("/{id}")
	@PreAuthorize("@per.hasPermission('sys:sysrole:read')")
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
	@PreAuthorize("@per.hasPermission('sys:sysrole:add')")
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
	@PreAuthorize("@per.hasPermission('sys:sysrole:edit')")
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
	@PreAuthorize("@per.hasPermission('sys:sysrole:del')")
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
	@PreAuthorize("@per.hasPermission('sys:sysrole:grant')")
	public R<Boolean> savePermissionIds(@PathVariable String roleCode, @RequestBody Integer[] permissionIds) {
		return R.ok(sysRolePermissionService.saveRolePermissions(roleCode, permissionIds));
	}

	/**
	 * 返回角色的菜单集合
	 * @param roleCode 角色ID
	 * @return 属性集合
	 */
	@GetMapping("/permission/code/{roleCode}")
	public R<List<Integer>> getPermissionIds(@PathVariable String roleCode) {
		return R.ok(sysPermissionService.listVOByRoleCode(roleCode).stream().map(PermissionVO::getId)
				.collect(Collectors.toList()));
	}

	/**
	 * 获取角色列表
	 * @return 角色列表
	 */
	@GetMapping("/select")
	public R<List<SelectData<?>>> listSelectData() {
		return R.ok(sysRoleService.listSelectData());
	}

}
