package com.hccake.ballcat.admin.modules.system.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.admin.constants.SysUserConst;
import com.hccake.ballcat.admin.modules.system.checker.AdminUserChecker;
import com.hccake.ballcat.admin.modules.system.converter.SysUserConverter;
import com.hccake.ballcat.admin.modules.system.event.UserChangeEvent;
import com.hccake.ballcat.admin.modules.system.mapper.SysUserMapper;
import com.hccake.ballcat.admin.modules.system.model.dto.SysUserDTO;
import com.hccake.ballcat.admin.modules.system.model.dto.SysUserScope;
import com.hccake.ballcat.admin.modules.system.model.dto.UserInfoDTO;
import com.hccake.ballcat.admin.modules.system.model.entity.SysMenu;
import com.hccake.ballcat.admin.modules.system.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.system.model.entity.SysUser;
import com.hccake.ballcat.admin.modules.system.model.qo.SysUserQO;
import com.hccake.ballcat.admin.modules.system.model.vo.SysUserPageVO;
import com.hccake.ballcat.admin.modules.system.service.SysMenuService;
import com.hccake.ballcat.admin.modules.system.service.SysRoleService;
import com.hccake.ballcat.admin.modules.system.service.SysUserRoleService;
import com.hccake.ballcat.admin.modules.system.service.SysUserService;
import com.hccake.ballcat.commom.oss.OssClient;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.domain.SelectData;
import com.hccake.ballcat.common.util.PasswordUtils;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
public class SysUserServiceImpl extends ExtendServiceImpl<SysUserMapper, SysUser> implements SysUserService {

	private final OssClient ossClient;

	private final SysMenuService sysMenuService;

	private final SysUserRoleService sysUserRoleService;

	private final AdminUserChecker adminUserChecker;

	private final SysRoleService sysRoleService;

	private final ApplicationEventPublisher publisher;

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<SysUserVO> 分页数据
	 */
	@Override
	public PageResult<SysUserPageVO> queryPage(PageParam pageParam, SysUserQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	/**
	 * 根据用户名查询用户
	 * @param username 用户名
	 * @return 系统用户
	 */
	@Override
	public SysUser getByUsername(String username) {
		return baseMapper.selectByUsername(username);
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

		// 超级管理员拥有所有角色
		List<SysRole> roleList;
		if (adminUserChecker.isAdminUser(sysUser)) {
			roleList = sysRoleService.list();
		}
		else {
			roleList = sysUserRoleService.listRoles(sysUser.getUserId());
		}

		// 设置角色标识
		List<String> roles = new ArrayList<>();
		for (SysRole role : roleList) {
			roles.add(role.getCode());
		}
		userInfoDTO.setRoles(roles);

		// 设置权限列表（permission）
		Set<String> permissions = new HashSet<>();
		for (String roleCode : roles) {
			List<String> permissionList = sysMenuService.listByRoleCode(roleCode).stream().map(SysMenu::getPermission)
					.filter(StrUtil::isNotEmpty).collect(Collectors.toList());
			permissions.addAll(permissionList);
		}
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
		// 对密码进行 BCrypt 加密
		String password = sysUserDto.getPassword();
		String bCryptPassword = PasswordUtils.encodeBCrypt(password);
		sysUser.setPassword(bCryptPassword);
		boolean result = SqlHelper.retBool(baseMapper.insert(sysUser));
		if (result) {
			publisher.publishEvent(new UserChangeEvent(sysUser));
		}
		return result;
	}

	/**
	 * 更新系统用户信息
	 * @param sysUserDTO 系统用户DTO
	 * @return 更新成功 true: 更新失败 false
	 */
	@Override
	public boolean updateSysUser(SysUserDTO sysUserDTO) {
		SysUser entity = SysUserConverter.INSTANCE.dtoToPo(sysUserDTO);
		Assert.isTrue(adminUserChecker.hasModifyPermission(entity), "当前用户不允许修改!");
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
	 * @param password 明文密码
	 * @return 更新成功：true
	 */
	@Override
	public boolean updatePassword(Integer userId, String password) {
		Assert.isTrue(adminUserChecker.hasModifyPermission(getById(userId)), "当前用户不允许修改!");
		// BCrypt 加密
		String bCryptPassword = PasswordUtils.encodeBCrypt(password);
		return baseMapper.updatePassword(userId, bCryptPassword);
	}

	/**
	 * 批量修改用户状态
	 * @param userIds 用户ID集合
	 * @return 更新成功：true
	 */
	@Override
	public boolean updateUserStatusBatch(List<Integer> userIds, Integer status) {

		List<SysUser> userList = baseMapper.listByUserIds(userIds);
		Assert.notEmpty(userList, "更新用户状态失败，待更新用户列表为空");

		// 移除无权限更改的用户id
		Map<Integer, SysUser> userMap = userList.stream()
				.collect(Collectors.toMap(SysUser::getUserId, Function.identity()));
		userIds.removeIf(id -> !adminUserChecker.hasModifyPermission(userMap.get(id)));
		Assert.notEmpty(userIds, "更新用户状态失败，无权限更新用户");

		return baseMapper.updateUserStatusBatch(userIds, status);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String updateAvatar(MultipartFile file, Integer userId) throws IOException {
		Assert.isTrue(adminUserChecker.hasModifyPermission(getById(userId)), "当前用户不允许修改!");
		// 获取系统用户头像的文件名
		String objectName = "sysuser/" + userId + "/avatar/" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
				+ StrUtil.SLASH + IdUtil.fastSimpleUUID() + StrUtil.DOT + FileUtil.extName(file.getOriginalFilename());
		objectName = ossClient.upload(file.getInputStream(), objectName, file.getSize());

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
	public List<SysUser> listByRoleCode(String roleCode) {
		return listByRoleCodes(Collections.singletonList(roleCode));
	}

	/**
	 * 根据角色查询用户
	 * @param roleCodes 角色标识集合
	 * @return List<SysUser>
	 */
	@Override
	public List<SysUser> listByRoleCodes(List<String> roleCodes) {
		return baseMapper.listByRoleCodes(roleCodes);
	}

	/**
	 * 根据组织机构ID查询用户
	 * @param organizationIds 组织机构id集合
	 * @return 用户集合
	 */
	@Override
	public List<SysUser> listByOrganizationIds(List<Integer> organizationIds) {
		return baseMapper.listByOrganizationIds(organizationIds);
	}

	/**
	 * 根据用户类型查询用户
	 * @param userTypes 用户类型集合
	 * @return 用户集合
	 */
	@Override
	public List<SysUser> listByUserTypes(List<Integer> userTypes) {
		return baseMapper.listByUserTypes(userTypes);
	}

	/**
	 * 根据用户Id集合查询用户
	 * @param userIds 用户Id集合
	 * @return 用户集合
	 */
	@Override
	public List<SysUser> listByUserIds(List<Integer> userIds) {
		return baseMapper.listByUserIds(userIds);

	}

	/**
	 * 返回用户的select数据 name=> username value => userId
	 * @return List<SelectData>
	 * @param userTypes 用户类型
	 */
	@Override
	public List<SelectData<?>> listSelectData(List<Integer> userTypes) {
		return baseMapper.listSelectData(userTypes);
	}

	/**
	 * 获取用户的角色Code集合
	 * @param userId 用户id
	 * @return List<String>
	 */
	@Override
	public List<String> listRoleCodes(Integer userId) {
		return sysUserRoleService.listRoles(userId).stream().map(SysRole::getCode).collect(Collectors.toList());
	}

}
