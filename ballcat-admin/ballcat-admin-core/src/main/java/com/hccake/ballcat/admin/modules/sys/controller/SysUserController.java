package com.hccake.ballcat.admin.modules.sys.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hccake.ballcat.admin.constants.SysUserConst;
import com.hccake.ballcat.admin.modules.sys.model.dto.SysUserDTO;
import com.hccake.ballcat.admin.modules.sys.model.dto.SysUserScope;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysUserQO;
import com.hccake.ballcat.admin.modules.sys.service.SysUserRoleService;
import com.hccake.ballcat.admin.modules.sys.service.SysUserService;
import com.hccake.ballcat.commom.log.operation.annotation.CreateOperationLogging;
import com.hccake.ballcat.commom.log.operation.annotation.DeleteOperationLogging;
import com.hccake.ballcat.commom.log.operation.annotation.UpdateOperationLogging;
import com.hccake.ballcat.common.core.result.BaseResultCode;
import com.hccake.ballcat.common.core.result.R;
import com.hccake.ballcat.common.core.result.SystemResultCode;
import com.hccake.ballcat.common.core.vo.SelectData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @date 2018/12/16
 */
@Slf4j
@RestController
@RequestMapping("/sysuser")
@Api(value = "sysuser", tags = "用户管理模块")
@RequiredArgsConstructor
public class SysUserController {

	private final SysUserService sysUserService;

	private final SysUserRoleService sysUserRoleService;

	/**
	 * 分页查询用户
<<<<<<< HEAD
=======
	 * <p>
	 * <p>
	 * ======= >>>>>>> 9e3178b... :art: 引入spring-java-format插件，代码格式管理
>>>>>>> dev
	 * @param page 参数集
	 * @return 用户集合
	 */
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('sys:sysuser:read')")
<<<<<<< HEAD
=======

>>>>>>> dev
	public R<IPage<SysUser>> getUserPage(Page<SysUser> page, SysUserQO qo) {
		return R.ok(sysUserService.page(page, qo));
	}

	/**
	 * 获取用户Select
	 * @return 用户SelectData
	 */
	@GetMapping("/select")
	@PreAuthorize("@per.hasPermission('sys:sysuser:read')")
<<<<<<< HEAD
	public R<List<SelectData>> getSelectData() {
=======

	public R<List<SelectData<?>>> getSelectData() {
>>>>>>> dev
		return R.ok(sysUserService.getSelectData(null));
	}

	/**
	 * 获取用户Select
	 * @return 用户SelectData
	 */
	@GetMapping("/select/{userType}")
	@PreAuthorize("@per.hasPermission('sys:sysuser:read')")
<<<<<<< HEAD
	public R<List<SelectData>> getSysSelectData(@PathVariable Integer userType) {
=======

	public R<List<SelectData<?>>> getSysSelectData(@PathVariable Integer userType) {
>>>>>>> dev
		return R.ok(sysUserService.getSelectData(userType));
	}

	/**
	 * 新增用户
	 * @param sysUserDto userInfo
	 * @return success/false
	 */
	@PostMapping
	@ApiOperation(value = "新增系统用户", notes = "新增系统用户")
	@CreateOperationLogging(msg = "新增系统用户")
	@PreAuthorize("@per.hasPermission('sys:sysuser:add')")
<<<<<<< HEAD
	public R addSysUser(@Valid @RequestBody SysUserDTO sysUserDto) {
=======

	public R<?> addSysUser(@Valid @RequestBody SysUserDTO sysUserDto) {
>>>>>>> dev

		SysUser user = sysUserService.getByUsername(sysUserDto.getUsername());
		if (user != null) {
			return R.failed(BaseResultCode.LOGIC_CHECK_ERROR, "用户名已存在");
		}
<<<<<<< HEAD
=======

>>>>>>> dev
		return sysUserService.addSysUser(sysUserDto) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增系统用户失败");
	}

	/**
	 * 修改用户个人信息
	 * @param sysUserDto userInfo
	 * @return success/false
	 */
	@PutMapping
	@ApiOperation(value = "修改系统用户", notes = "修改系统用户")
	@UpdateOperationLogging(msg = "修改系统用户")
	@PreAuthorize("@per.hasPermission('sys:sysuser:edit')")
<<<<<<< HEAD
	public R updateUserInfo(@Valid @RequestBody SysUserDTO sysUserDto) {
=======

	public R<?> updateUserInfo(@Valid @RequestBody SysUserDTO sysUserDto) {
>>>>>>> dev
		return sysUserService.updateSysUser(sysUserDto) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改系统用户失败");
	}

	/**
	 * 删除用户信息
<<<<<<< HEAD
	 * @param userId
	 * @return
=======
>>>>>>> dev
	 */
	@DeleteMapping("/{userId}")
	@ApiOperation(value = "通过id删除系统用户", notes = "通过id删除系统用户")
	@DeleteOperationLogging(msg = "通过id删除系统用户")
	@PreAuthorize("@per.hasPermission('sys:sysuser:del')")
<<<<<<< HEAD
	public R deleteByUserId(@PathVariable Integer userId) {
=======

	public R<?> deleteByUserId(@PathVariable Integer userId) {
>>>>>>> dev
		return sysUserService.deleteByUserId(userId) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "删除系统用户失败");
	}

	/**
	 * 获取用户 所拥有的角色ID
	 * @param userId userId
	 */
	@GetMapping("/scope/{userId}")
	@PreAuthorize("@per.hasPermission('sys:sysuser:grant')")
	public R<SysUserScope> getUserRoleIds(@PathVariable Integer userId) {

		List<SysRole> roleList = sysUserRoleService.getRoles(userId);

		List<Integer> roleIds = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(roleList)) {
			for (SysRole role : roleList) {
				roleIds.add(role.getId());
			}
		}

		SysUserScope sysUserScope = new SysUserScope();
		sysUserScope.setRoleIds(roleIds);

		return R.ok(sysUserScope);
	}

	/**
	 * 修改用户权限信息 比如角色 数据权限等
	 * @param sysUserScope sysUserScope
	 * @return success/false
	 */
	@PutMapping("/scope/{userId}")
	@ApiOperation(value = "系统用户授权", notes = "系统用户授权")
	@UpdateOperationLogging(msg = "系统用户授权")
	@PreAuthorize("@per.hasPermission('sys:sysuser:grant')")
<<<<<<< HEAD
	public R updateUserScope(@PathVariable Integer userId, @RequestBody SysUserScope sysUserScope) {
=======

	public R<?> updateUserScope(@PathVariable Integer userId, @RequestBody SysUserScope sysUserScope) {
>>>>>>> dev
		return sysUserService.updateUserScope(userId, sysUserScope) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "系统用户授权失败");
	}

	/**
	 * 修改用户密码
<<<<<<< HEAD
	 * @param userId
	 * @param pass
	 * @param confirm
	 * @return
=======
>>>>>>> dev
	 */
	@PutMapping("/pass/{userId}")
	@ApiOperation(value = "修改系统用户密码", notes = "修改系统用户密码")
	@UpdateOperationLogging(msg = "修改系统用户密码")
	@PreAuthorize("@per.hasPermission('sys:sysuser:pass')")
<<<<<<< HEAD
	public R updateUserPass(@PathVariable Integer userId, String pass, String confirm) {
=======

	public R<?> updateUserPass(@PathVariable Integer userId, String pass, String confirm) {
>>>>>>> dev
		if (StrUtil.isBlank(pass) || StrUtil.isBlank(confirm) || !pass.equals(confirm)) {
			return R.failed(SystemResultCode.BAD_REQUEST, "错误的密码!");
		}

		return sysUserService.updateUserPass(userId, pass) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改用户密码失败！");
	}

	/**
	 * 批量修改用户状态
<<<<<<< HEAD
	 * @param userIds
	 * @return
=======
>>>>>>> dev
	 */
	@PutMapping("/status")
	@ApiOperation(value = "批量修改用户状态", notes = "批量修改用户状态")
	@UpdateOperationLogging(msg = "批量修改用户状态")
	@PreAuthorize("@per.hasPermission('sys:sysuser:edit')")
<<<<<<< HEAD
	public R updateUserStatus(@NotEmpty(message = "用户ID不能为空") @RequestBody List<Integer> userIds,
=======

	public R<?> updateUserStatus(@NotEmpty(message = "用户ID不能为空") @RequestBody List<Integer> userIds,
>>>>>>> dev
			@NotNull(message = "用户状态不能为空") @RequestParam Integer status) {

		if (!SysUserConst.Status.NORMAL.getValue().equals(status)
				&& !SysUserConst.Status.LOCKED.getValue().equals(status)) {
			throw new ValidationException("不支持的用户状态！");
		}
		return sysUserService.updateUserStatus(userIds, status) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "批量修改用户状态！");
	}

	@ApiOperation(value = "修改系统用户头像", notes = "修改系统用户头像")
	@UpdateOperationLogging(msg = "修改系统用户头像")
	@PreAuthorize("@per.hasPermission('sys:sysuser:edit')")
	@PostMapping("/avatar")
	public R<String> updateAvatar(@RequestParam("file") MultipartFile file, @RequestParam("userId") Integer userId) {
		String objectName;
		try {
			objectName = sysUserService.updateAvatar(file, userId);
		}
		catch (IOException e) {
			log.error("修改系统用户头像异常", e);
			return R.failed(BaseResultCode.FILE_UPLOAD_ERROR);
		}
<<<<<<< HEAD
		return R.ok(objectName);
=======
		return StrUtil.isEmpty(objectName) ? R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改系统头像失败!")
				: R.ok(objectName);
>>>>>>> dev
	}

}
