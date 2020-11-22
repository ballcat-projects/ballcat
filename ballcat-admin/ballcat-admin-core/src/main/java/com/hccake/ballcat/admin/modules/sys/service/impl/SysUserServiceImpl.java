package com.hccake.ballcat.admin.modules.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.admin.constants.SysUserConst;
import com.hccake.ballcat.admin.modules.sys.checker.AdminUserChecker;
import com.hccake.ballcat.admin.modules.sys.mapper.SysUserMapper;
import com.hccake.ballcat.admin.modules.sys.model.converter.SysUserConverter;
import com.hccake.ballcat.admin.modules.sys.model.dto.SysUserDTO;
import com.hccake.ballcat.admin.modules.sys.model.dto.SysUserScope;
import com.hccake.ballcat.admin.modules.sys.model.dto.UserInfoDTO;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysUserQO;
import com.hccake.ballcat.admin.modules.sys.model.vo.PermissionVO;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysUserVO;
import com.hccake.ballcat.admin.modules.sys.service.*;
import com.hccake.ballcat.admin.oauth.util.SecurityUtils;
import com.hccake.ballcat.common.core.util.PasswordUtil;
import com.hccake.ballcat.common.core.vo.SelectData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 系统用户表
 *
 * @author ballcat code generator
 * @date 2019-09-12 20:39:31
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

	private final FileService fileService;

	private final SysPermissionService sysPermissionService;

	private final SysUserRoleService sysUserRoleService;

	private final AdminUserChecker adminUserChecker;

	private final SysRoleService sysRoleService;

	@Value("${password.secret-key}")
	private String secretKey;

	private final static String TABLE_ALIAS_PREFIX = "su.";

	/**
	 * 根据QueryObject查询分页数据
	 * @param page 分页参数
	 * @param qo 查询参数对象
	 * @return IPage<SysUserVO> 分页数据
	 */
	@Override
	public IPage<SysUserVO> selectPageVo(IPage<?> page, SysUserQO qo) {

		QueryWrapper<SysUser> wrapper = Wrappers.<SysUser>query().eq(TABLE_ALIAS_PREFIX + "deleted", 0)
				.like(ObjectUtil.isNotNull(qo.getUsername()), TABLE_ALIAS_PREFIX + "username", qo.getUsername())
				.like(ObjectUtil.isNotNull(qo.getEmail()), TABLE_ALIAS_PREFIX + "email", qo.getEmail())
				.like(ObjectUtil.isNotNull(qo.getPhone()), TABLE_ALIAS_PREFIX + "phone", qo.getPhone())
				.like(ObjectUtil.isNotNull(qo.getNickname()), TABLE_ALIAS_PREFIX + "nickname", qo.getNickname())
				.eq(ObjectUtil.isNotNull(qo.getStatus()), TABLE_ALIAS_PREFIX + "status", qo.getStatus())
				.eq(ObjectUtil.isNotNull(qo.getSex()), TABLE_ALIAS_PREFIX + "sex", qo.getSex())
				.eq(ObjectUtil.isNotNull(qo.getType()), TABLE_ALIAS_PREFIX + "type", qo.getType())
				.in(CollectionUtil.isNotEmpty(qo.getOrganizationId()), TABLE_ALIAS_PREFIX + "organization_id",
						qo.getOrganizationId());
		if (StringUtils.isNotBlank(qo.getStartTime()) && StringUtils.isNotBlank(qo.getEndTime())) {
			wrapper.between(TABLE_ALIAS_PREFIX + "create_time", qo.getStartTime(), qo.getEndTime());
		}
		return baseMapper.selectPageVo(page, wrapper);
	}

	/**
	 * 根据用户名查询用户
	 * @param username 用户名
	 * @return 系统用户
	 */
	@Override
	public SysUser getByUsername(String username) {
		return baseMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
	}

	/**
	 * 通过查用户的全部信息
	 * @param sysUser 用户
	 * @return 用户信息
	 */
	@Override
	public UserInfoDTO findUserInfo(SysUser sysUser) {
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		userInfoDTO.setSysUser(sysUser);
		// 设置角色列表 （ID）
		List<SysRole> roleList;

		if (adminUserChecker.isAdminUser(sysUser)) {
			// 超级管理员拥有所有角色
			roleList = sysRoleService.list();
		}
		else {
			roleList = sysUserRoleService.getRoles(sysUser.getUserId());
		}

		List<Integer> roleIds = new ArrayList<>();
		List<String> roles = new ArrayList<>();
		for (SysRole role : roleList) {
			roleIds.add(role.getId());
			roles.add(role.getCode());
		}

		userInfoDTO.setRoles(roles);
		userInfoDTO.setRoleIds(roleIds);

		// 设置权限列表（permission）
		Set<String> permissions = new HashSet<>();
		roles.forEach(code -> {
			List<String> permissionList = sysPermissionService.findPermissionVOsByRoleCode(code).stream()
					.filter(sysPermission -> StrUtil.isNotEmpty(sysPermission.getCode())).map(PermissionVO::getCode)
					.collect(Collectors.toList());
			permissions.addAll(permissionList);
		});
		userInfoDTO.setPermissions(new ArrayList<>(permissions));
		return userInfoDTO;
	}

	/**
	 * 新增系统用户
	 * @param sysUserDto 系统用户DTO
	 * @return 添加成功：true , 失败：false
	 */
	@Override
	public boolean addSysUser(SysUserDTO sysUserDto) {
		SysUser sysUser = SysUserConverter.INSTANCE.dtoToPo(sysUserDto);
		sysUser.setStatus(SysUserConst.Status.NORMAL.getValue());
		sysUser.setType(SysUserConst.Type.SYSTEM.getValue());

		String password = PasswordUtil.decodeAesAndEncodeBCrypt(sysUserDto.getPass(), secretKey);
		sysUser.setPassword(password);

		return SqlHelper.retBool(baseMapper.insert(sysUser));
	}

	/**
	 * 更新系统用户信息
	 * @param sysUserDTO 系统用户DTO
	 * @return 更新成功 true: 更新失败 false
	 */
	@Override
	public boolean updateSysUser(SysUserDTO sysUserDTO) {
		SysUser entity = SysUserConverter.INSTANCE.dtoToPo(sysUserDTO);
		Assert.isTrue(hasModifyPermission(entity), "当前用户不允许修改!");
		return SqlHelper.retBool(baseMapper.updateById(entity));
	}

	/**
	 * 更新用户权限信息
	 * @param userId 用户Id
	 * @param sysUserScope 系统用户权限范围
	 * @return 更新成功：true
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateUserScope(Integer userId, SysUserScope sysUserScope) {
		// 更新用户角色关联关系
		// TODO 在这里实现 自己业务 模块的权限控制
		return sysUserRoleService.updateUserRoles(userId, sysUserScope.getRoleCodes());

	}

	/**
	 * 根据userId删除 用户
	 * @param userId 用户ID
	 * @return 删除成功：true
	 */
	@Override
	public boolean deleteByUserId(Integer userId) {
		// TODO 缓存控制
		Assert.isFalse(adminUserChecker.isAdminUser(getById(userId)), "管理员不允许删除!");
		return SqlHelper.retBool(baseMapper.deleteById(userId));
	}

	/**
	 * 修改用户密码
	 * @param userId 用户ID
	 * @param pass 明文密码
	 * @return 更新成功：true
	 */
	@Override
	public boolean updateUserPass(Integer userId, String pass) {
		Assert.isTrue(hasModifyPermission(getById(userId)), "当前用户不允许修改!");
		String password = PasswordUtil.decodeAesAndEncodeBCrypt(pass, secretKey);

		int res = baseMapper.update(null,
				Wrappers.<SysUser>lambdaUpdate().eq(SysUser::getUserId, userId).set(SysUser::getPassword, password));

		return SqlHelper.retBool(res);
	}

	/**
	 * 修改权限校验
	 * @param targetUser 目标用户
	 * @return 是否有权限修改目标用户
	 */
	private boolean hasModifyPermission(SysUser targetUser) {
		// 如果需要修改的用户是超级管理员，则只能本人修改
		if (adminUserChecker.isAdminUser(targetUser)) {
			return SecurityUtils.getSysUserDetails().getUsername().equals(targetUser.getUsername());
		}
		return true;
	}

	/**
	 * 批量修改用户状态
	 * @param userIds 用户ID集合
	 * @return 更新成功：true
	 */
	@Override
	public boolean updateUserStatus(List<Integer> userIds, Integer status) {

		List<SysUser> userList = baseMapper.selectList(Wrappers.<SysUser>lambdaQuery().in(SysUser::getUserId, userIds));
		Assert.notEmpty(userList, "更新用户状态失败，待更新用户列表为空");

		// 移除无权限更改的用户id
		Map<Integer, SysUser> userMap = userList.stream()
				.collect(Collectors.toMap(SysUser::getUserId, Function.identity()));
		userIds.removeIf(id -> !hasModifyPermission(userMap.get(id)));
		Assert.notEmpty(userIds, "更新用户状态失败，无权限更新用户");

		return this.update(
				Wrappers.<SysUser>lambdaUpdate().set(SysUser::getStatus, status).in(SysUser::getUserId, userIds));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String updateAvatar(MultipartFile file, Integer userId) throws IOException {
		Assert.isTrue(hasModifyPermission(getById(userId)), "当前用户不允许修改!");
		// 获取系统用户头像的文件名
		String objectName = "sysuser/" + userId + "/avatar/" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
				+ StrUtil.SLASH + IdUtil.fastSimpleUUID() + StrUtil.DOT + FileUtil.extName(file.getOriginalFilename());
		fileService.uploadFile(file, objectName);

		SysUser sysUser = new SysUser();
		sysUser.setUserId(userId);
		sysUser.setAvatar(objectName);
		baseMapper.updateById(sysUser);

		return objectName;
	}

	/**
	 * 根据角色查询用户
	 * @param roleCode 角色标识
	 * @return 系统用户集合
	 */
	@Override
	public List<SysUser> selectUsersByRoleCode(String roleCode) {
		return baseMapper.selectUsersByRoleCode(roleCode);
	}

	/**
	 * 返回用户的select数据 name=> username value => userId
	 * @return List<SelectData>
	 * @param userTypes 用户类型
	 */
	@Override
	public List<SelectData<?>> getSelectData(List<Integer> userTypes) {
		return baseMapper.getSelectData(userTypes);
	}

}
