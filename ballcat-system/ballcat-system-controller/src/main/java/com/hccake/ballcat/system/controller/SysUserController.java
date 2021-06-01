package com.hccake.ballcat.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.commom.log.operation.annotation.CreateOperationLogging;
import com.hccake.ballcat.commom.log.operation.annotation.DeleteOperationLogging;
import com.hccake.ballcat.commom.log.operation.annotation.UpdateOperationLogging;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.domain.SelectData;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.common.model.result.SystemResultCode;
import com.hccake.ballcat.common.util.PasswordUtils;
import com.hccake.ballcat.oauth.properties.SecurityProperties;
import com.hccake.ballcat.system.constant.SysUserConst;
import com.hccake.ballcat.system.converter.SysUserConverter;
import com.hccake.ballcat.system.model.dto.SysUserDTO;
import com.hccake.ballcat.system.model.dto.SysUserPassDTO;
import com.hccake.ballcat.system.model.dto.SysUserScope;
import com.hccake.ballcat.system.model.entity.SysRole;
import com.hccake.ballcat.system.model.entity.SysUser;
import com.hccake.ballcat.system.model.qo.SysUserQO;
import com.hccake.ballcat.system.model.vo.SysUserInfo;
import com.hccake.ballcat.system.model.vo.SysUserPageVO;
import com.hccake.ballcat.system.service.SysUserRoleService;
import com.hccake.ballcat.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
 * 组织架构
 *
 * @author hccake 2020-09-24 20:16:15
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/system/user")
@Api(value = "system-user", tags = "用户管理模块")
@RequiredArgsConstructor
public class SysUserController {

	private final SysUserService sysUserService;

	private final SysUserRoleService sysUserRoleService;

	private final SecurityProperties securityProperties;

	/**
	 * 分页查询用户
	 * @param pageParam 参数集
	 * @return 用户集合
	 */
	@ApiOperation(value = "分页查询系统用户")
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('system:user:read')")
	public R<PageResult<SysUserPageVO>> getUserPage(PageParam pageParam, SysUserQO qo) {
		return R.ok(sysUserService.queryPage(pageParam, qo));
	}

	/**
	 * 获取用户Select
	 * @return 用户SelectData
	 */
	@ApiOperation(value = "获取用户下拉列表数据")
	@GetMapping("/select")
	@PreAuthorize("@per.hasPermission('system:user:read')")
	public R<List<SelectData<?>>> listSelectData(
			@RequestParam(value = "userTypes", required = false) List<Integer> userTypes) {
		return R.ok(sysUserService.listSelectData(userTypes));
	}

	/**
	 * 获取指定用户的基本信息
	 * @param userId 用户ID
	 * @return SysUserInfo
	 */
	@ApiOperation(value = "获取指定用户的基本信息")
	@GetMapping("/{userId}")
	@PreAuthorize("@per.hasPermission('system:user:read')")
	public R<SysUserInfo> getSysUserInfo(@PathVariable("userId") Integer userId) {
		SysUser sysUser = sysUserService.getById(userId);
		if (sysUser == null) {
			return R.ok();
		}
		SysUserInfo sysUserInfo = SysUserConverter.INSTANCE.poToInfo(sysUser);
		return R.ok(sysUserInfo);
	}

	/**
	 * 新增用户
	 * @param sysUserDTO userInfo
	 * @return success/false
	 */
	@PostMapping
	@ApiOperation(value = "新增系统用户", notes = "新增系统用户")
	@CreateOperationLogging(msg = "新增系统用户")
	@PreAuthorize("@per.hasPermission('system:user:add')")
	public R<?> addSysUser(@Valid @RequestBody SysUserDTO sysUserDTO) {
		SysUser user = sysUserService.getByUsername(sysUserDTO.getUsername());
		if (user != null) {
			return R.failed(BaseResultCode.LOGIC_CHECK_ERROR, "用户名已存在");
		}
		// 明文密码
		String password = PasswordUtils.decodeAES(sysUserDTO.getPass(), securityProperties.getPasswordSecretKey());
		sysUserDTO.setPassword(password);
		return sysUserService.addSysUser(sysUserDTO) ? R.ok()
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
	@PreAuthorize("@per.hasPermission('system:user:edit')")
	public R<?> updateUserInfo(@Valid @RequestBody SysUserDTO sysUserDto) {
		return sysUserService.updateSysUser(sysUserDto) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改系统用户失败");
	}

	/**
	 * 删除用户信息
	 */
	@DeleteMapping("/{userId}")
	@ApiOperation(value = "通过id删除系统用户", notes = "通过id删除系统用户")
	@DeleteOperationLogging(msg = "通过id删除系统用户")
	@PreAuthorize("@per.hasPermission('system:user:del')")
	public R<?> deleteByUserId(@PathVariable("userId") Integer userId) {
		return sysUserService.deleteByUserId(userId) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "删除系统用户失败");
	}

	/**
	 * 获取用户 所拥有的角色ID
	 * @param userId userId
	 */
	@GetMapping("/scope/{userId}")
	@PreAuthorize("@per.hasPermission('system:user:grant')")
	public R<SysUserScope> getUserRoleIds(@PathVariable("userId") Integer userId) {

		List<SysRole> roleList = sysUserRoleService.listRoles(userId);

		List<String> roleCodes = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(roleList)) {
			roleList.forEach(role -> roleCodes.add(role.getCode()));
		}

		SysUserScope sysUserScope = new SysUserScope();
		sysUserScope.setRoleCodes(roleCodes);

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
	@PreAuthorize("@per.hasPermission('system:user:grant')")
	public R<?> updateUserScope(@PathVariable("userId") Integer userId, @RequestBody SysUserScope sysUserScope) {
		return sysUserService.updateUserScope(userId, sysUserScope) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "系统用户授权失败");
	}

	/**
	 * 修改用户密码
	 */
	@PutMapping("/pass/{userId}")
	@ApiOperation(value = "修改系统用户密码", notes = "修改系统用户密码")
	@UpdateOperationLogging(msg = "修改系统用户密码")
	@PreAuthorize("@per.hasPermission('system:user:pass')")
	public R<?> updateUserPass(@PathVariable("userId") Integer userId, @RequestBody SysUserPassDTO sysUserPassDTO) {
		String pass = sysUserPassDTO.getPass();
		if (!pass.equals(sysUserPassDTO.getConfirmPass())) {
			return R.failed(SystemResultCode.BAD_REQUEST, "错误的密码!");
		}

		// 明文密码
		String password = PasswordUtils.decodeAES(pass, securityProperties.getPasswordSecretKey());
		return sysUserService.updatePassword(userId, password) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改用户密码失败！");
	}

	/**
	 * 批量修改用户状态
	 */
	@PutMapping("/status")
	@ApiOperation(value = "批量修改用户状态", notes = "批量修改用户状态")
	@UpdateOperationLogging(msg = "批量修改用户状态")
	@PreAuthorize("@per.hasPermission('system:user:edit')")
	public R<?> updateUserStatus(@NotEmpty(message = "用户ID不能为空") @RequestBody List<Integer> userIds,
			@NotNull(message = "用户状态不能为空") @RequestParam("status") Integer status) {

		if (!SysUserConst.Status.NORMAL.getValue().equals(status)
				&& !SysUserConst.Status.LOCKED.getValue().equals(status)) {
			throw new ValidationException("不支持的用户状态！");
		}
		return sysUserService.updateUserStatusBatch(userIds, status) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "批量修改用户状态！");
	}

	@ApiOperation(value = "修改系统用户头像", notes = "修改系统用户头像")
	@UpdateOperationLogging(msg = "修改系统用户头像")
	@PreAuthorize("@per.hasPermission('system:user:edit')")
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
		return R.ok(objectName);
	}

}
