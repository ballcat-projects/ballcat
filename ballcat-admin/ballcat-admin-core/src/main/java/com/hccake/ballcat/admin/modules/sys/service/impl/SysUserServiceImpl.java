package com.hccake.ballcat.admin.modules.sys.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.admin.constants.SysUserConst;
import com.hccake.ballcat.admin.modules.sys.mapper.SysUserMapper;
import com.hccake.ballcat.admin.modules.sys.model.converter.SysUserConverter;
import com.hccake.ballcat.admin.modules.sys.model.dto.SysUserDTO;
import com.hccake.ballcat.admin.modules.sys.model.dto.SysUserScope;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysUserQO;
import com.hccake.ballcat.admin.modules.sys.model.vo.PermissionVO;
import com.hccake.ballcat.admin.modules.sys.model.vo.UserInfo;
import com.hccake.ballcat.admin.modules.sys.service.*;
import com.hccake.ballcat.admin.oauth.util.SecurityUtils;
import com.hccake.ballcat.common.core.constant.GlobalConstants;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

	private final SysAdminConfigService sysAdminConfigService;

	private final SysRoleService sysRoleService;

	@Value("${password.secret-key}")
	private String secretKey;

	@Override
	public IPage<SysUser> page(IPage<SysUser> page, SysUserQO qo) {

		LambdaQueryWrapper<SysUser> wrapper = Wrappers.<SysUser>lambdaQuery()
				.like(ObjectUtil.isNotNull(qo.getUsername()), SysUser::getUsername, qo.getUsername())
				.like(ObjectUtil.isNotNull(qo.getEmail()), SysUser::getEmail, qo.getEmail())
				.like(ObjectUtil.isNotNull(qo.getPhone()), SysUser::getPhone, qo.getPhone())
				.like(ObjectUtil.isNotNull(qo.getNickname()), SysUser::getNickname, qo.getNickname())
				.eq(ObjectUtil.isNotNull(qo.getStatus()), SysUser::getStatus, qo.getStatus())
				.eq(ObjectUtil.isNotNull(qo.getSex()), SysUser::getSex, qo.getSex())
				.eq(ObjectUtil.isNotNull(qo.getType()), SysUser::getType, qo.getType());
		if (StringUtils.isNotBlank(qo.getStartTime()) && StringUtils.isNotBlank(qo.getEndTime())) {
			wrapper.between(SysUser::getCreateTime, qo.getStartTime(), qo.getEndTime());
		}

		return baseMapper.selectPage(page, wrapper);
	}

	/**
	 * 根据用户名查询用户
	 * @param username
	 * @return
	 */
	@Override
	public SysUser getByUsername(String username) {
		return baseMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
	}

	/**
	 * 通过查用户的全部信息
	 * @param sysUser 用户
	 * @return
	 */
	@Override
	public UserInfo findUserInfo(SysUser sysUser) {
		UserInfo userInfo = new UserInfo();
		userInfo.setSysUser(sysUser);
		// 设置角色列表 （ID）
		List<SysRole> roleList;

		if (sysAdminConfigService.verify(sysUser)) {
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

		userInfo.setRoles(roles);
		userInfo.setRoleIds(roleIds);

		// 设置权限列表（permission）
		Set<String> permissions = new HashSet<>();
		roleIds.forEach(roleId -> {
			List<String> permissionList = sysPermissionService.findPermissionVOByRoleId(roleId).stream()
					.filter(sysPermission -> StrUtil.isNotEmpty(sysPermission.getCode())).map(PermissionVO::getCode)
					.collect(Collectors.toList());
			permissions.addAll(permissionList);
		});
		userInfo.setPermissions(new ArrayList<>(permissions));
		return userInfo;
	}

	/**
	 * 新增系统用户
	 * @param sysUserDto
	 * @return
	 */
	@Override
	public boolean addSysUser(SysUserDTO sysUserDto) {
		SysUser sysUser = SysUserConverter.INSTANCE.dtoToPo(sysUserDto);
		sysUser.setStatus(SysUserConst.Status.NORMAL.getValue());
		sysUser.setType(SysUserConst.Type.SYSTEM.getValue());
		sysUser.setDeleted(GlobalConstants.NOT_DELETED_FLAG);

		String password = PasswordUtil.decodeAesAndEncodeBCrypt(sysUserDto.getPass(), secretKey);
		sysUser.setPassword(password);

		return SqlHelper.retBool(baseMapper.insert(sysUser));
	}

	/**
	 * 更新系统用户信息
	 * @param sysUserDTO
	 * @return
	 */
	@Override
	public boolean updateSysUser(SysUserDTO sysUserDTO) {
		SysUser entity = SysUserConverter.INSTANCE.dtoToPo(sysUserDTO);
		if (!isSelf(entity)) {
			return false;
		}
		return SqlHelper.retBool(baseMapper.updateById(entity));
	}

	/**
	 * 更新用户权限信息
	 * @param userId
	 * @param sysUserScope
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateUserScope(Integer userId, SysUserScope sysUserScope) {
		// 更新用户角色关联关系
		// TODO 在这里实现 自己业务 模块的权限控制
		return sysUserRoleService.updateUserRoles(userId, sysUserScope.getRoleIds());

	}

	/**
	 * 根据userId删除 用户
	 * @param userId
	 * @return
	 */
	@Override
	public boolean deleteByUserId(Integer userId) {
		if (sysAdminConfigService.verify(getById(userId))) {
			// 无法删除超级管理员
			return false;
		}
		// TODO 缓存控制
		return SqlHelper.retBool(baseMapper.deleteById(userId));
	}

	/**
	 * 修改用户密码
	 * @param userId
	 * @param pass
	 * @return
	 */
	@Override
	public boolean updateUserPass(Integer userId, String pass) {
		if (!isSelf(getById(userId))) {
			return false;
		}
		String password = PasswordUtil.decodeAesAndEncodeBCrypt(pass, secretKey);

		int res = baseMapper.update(null,
				Wrappers.<SysUser>lambdaUpdate().eq(SysUser::getUserId, userId).set(SysUser::getPassword, password));

		return SqlHelper.retBool(res);
	}

	/**
	 * 判断指定用户是否为超级管理员 如果是，则判断指定用户与当前为同一个用户
	 * @return 指定用户不是超级管理员 返回 true , 如果指定用户时超级管理员 且 指定用户为当前登录用户 返回true
	 * @author lingting 2020-06-24 21:24:53
	 */
	private boolean isSelf(SysUser user) {
		if (!sysAdminConfigService.verify(user)) {
			return true;
		}
		// 要修改的用户是超级管理员， 但是修改人不是本人
		return !SecurityUtils.getSysUserDetails().getUsername().equals(user.getUsername());
	}

	/**
	 * 批量修改用户状态
	 * @param userIds
	 * @return
	 */
	@Override
	public boolean updateUserStatus(List<Integer> userIds, Integer status) {
		new ArrayList<>(userIds).forEach(id -> {
			SysUser entity = getById(id);
			if (sysAdminConfigService.verify(entity)
					&& !SecurityUtils.getSysUserDetails().getUsername().equals(entity.getUsername())) {
				// 要修改的用户是超级管理员， 但是修改人不是本人, 从修改列表中移除
				userIds.remove(id);
			}
		});
		return this.update(
				Wrappers.<SysUser>lambdaUpdate().set(SysUser::getStatus, status).in(SysUser::getUserId, userIds));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String updateAvatar(MultipartFile file, Integer userId) throws IOException {
		if (isSelf(getById(userId))) {
			return StrUtil.EMPTY;
		}
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
	 * @param roleCode
	 * @return
	 */
	@Override
	public List<SysUser> selectUsersByRoleCode(String roleCode) {
		return baseMapper.selectUsersByRoleCode(roleCode);
	}

	/**
	 * 返回用户的select数据 name=> username value => userId
	 * @return List<SelectData>
	 */
	@Override
	public List<SelectData<?>> getSelectData(Integer type) {
		return baseMapper.getSelectData(type);
	}

}
