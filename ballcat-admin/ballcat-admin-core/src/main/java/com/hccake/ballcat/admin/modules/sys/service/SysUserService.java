package com.hccake.ballcat.admin.modules.sys.service;

import com.hccake.ballcat.admin.modules.sys.model.dto.SysUserDTO;
import com.hccake.ballcat.admin.modules.sys.model.dto.SysUserScope;
import com.hccake.ballcat.admin.modules.sys.model.dto.UserInfoDTO;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysUserQO;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysUserPageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.domain.SelectData;
import com.hccake.extend.mybatis.plus.service.ExtendService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 系统用户表
 *
 * @author ballcat code generator
 * @date 2019-09-12 20:39:31
 */
public interface SysUserService extends ExtendService<SysUser> {

	/**
	 * 根据QueryObject查询系统用户列表
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<SysUserVO> 分页数据
	 */
	PageResult<SysUserPageVO> queryPage(PageParam pageParam, SysUserQO qo);

	/**
	 * 根据用户名查询用户
	 * @param username 用户名
	 * @return SysUser
	 */
	SysUser getByUsername(String username);

	/**
	 * 获取用户详情信息
	 * @param user SysUser
	 * @return UserInfoDTO
	 */
	UserInfoDTO findUserInfo(SysUser user);

	/**
	 * 新增系统用户
	 * @param sysUserDto SysUserDTO
	 * @return boolean
	 */
	boolean addSysUser(SysUserDTO sysUserDto);

	/**
	 * 更新系统用户信息
	 * @param sysUserDTO 用户DTO
	 * @return boolean
	 */
	boolean updateSysUser(SysUserDTO sysUserDTO);

	/**
	 * 更新用户权限信息
	 * @param userId 用户ID
	 * @param sysUserScope 用户权限域
	 * @return boolean
	 */
	boolean updateUserScope(Integer userId, SysUserScope sysUserScope);

	/**
	 * 根据userId删除 用户
	 * @param userId 用户ID
	 * @return boolean
	 */
	boolean deleteByUserId(Integer userId);

	/**
	 * 修改用户密码
	 * @param userId 用户ID
	 * @param password 明文密码
	 * @return boolean
	 */
	boolean updatePassword(Integer userId, String password);

	/**
	 * 批量修改用户状态
	 * @param userIds 用户ID集合
	 * @param status 状态
	 * @return boolean
	 */
	boolean updateUserStatusBatch(List<Integer> userIds, Integer status);

	/**
	 * 修改系统用户头像
	 * @param file 头像文件
	 * @param userId 用户ID
	 * @return 文件相对路径
	 * @throws IOException IO异常
	 */
	String updateAvatar(MultipartFile file, Integer userId) throws IOException;

	/**
	 * 根据角色查询用户
	 * @param roleCode 角色标识
	 * @return List<SysUser>
	 */
	List<SysUser> listByRoleCode(String roleCode);

	/**
	 * 根据角色查询用户
	 * @param roleCodes 角色标识集合
	 * @return List<SysUser> 用户集合
	 */
	List<SysUser> listByRoleCodes(List<String> roleCodes);

	/**
	 * 根据组织机构ID查询用户
	 * @param organizationIds 组织机构id集合
	 * @return 用户集合
	 */
	List<SysUser> listByOrganizationIds(List<Integer> organizationIds);

	/**
	 * 根据用户类型查询用户
	 * @param userTypes 用户类型集合
	 * @return 用户集合
	 */
	List<SysUser> listByUserTypes(List<Integer> userTypes);

	/**
	 * 根据用户Id集合查询用户
	 * @param userIds 用户Id集合
	 * @return 用户集合
	 */
	List<SysUser> listByUserIds(List<Integer> userIds);

	/**
	 * 返回用户的select数据
	 * @param type 为空时返回所有客户为1返回系统客户 name=> username value => userId
	 * @return List<SelectData>
	 */
	List<SelectData<?>> listSelectData(List<Integer> type);

	/**
	 * 获取用户的角色Code集合
	 * @param userId 用户id
	 * @return List<String>
	 */
	List<String> listRoleCodes(Integer userId);

}
