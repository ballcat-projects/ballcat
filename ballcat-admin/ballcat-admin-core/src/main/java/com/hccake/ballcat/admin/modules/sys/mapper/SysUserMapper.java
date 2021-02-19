package com.hccake.ballcat.admin.modules.sys.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysUserQO;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysUserVO;
import com.hccake.ballcat.common.core.constant.GlobalConstants;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.domain.PageResult;
import com.hccake.ballcat.common.core.domain.SelectData;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaAliasQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户表
 *
 * @author Hccake
 */
public interface SysUserMapper extends ExtendMapper<SysUser> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return PageResult<SysUserVO>
	 */
	default PageResult<SysUserVO> queryPage(PageParam pageParam, SysUserQO qo) {
		IPage<SysUserVO> page = this.prodPage(pageParam);
		LambdaAliasQueryWrapperX<SysUser> wrapperX = WrappersX.lambdaAliasQueryX(SysUser.class);
		wrapperX.eq(SysUser::getDeleted, GlobalConstants.NOT_DELETED_FLAG)
				.likeIfPresent(SysUser::getUsername, qo.getUsername()).likeIfPresent(SysUser::getEmail, qo.getEmail())
				.likeIfPresent(SysUser::getPhone, qo.getPhone()).likeIfPresent(SysUser::getNickname, qo.getNickname())
				.eqIfPresent(SysUser::getStatus, qo.getStatus()).eqIfPresent(SysUser::getSex, qo.getSex())
				.eqIfPresent(SysUser::getType, qo.getType())
				.inIfPresent(SysUser::getOrganizationId, qo.getOrganizationId());
		if (StringUtils.isNotBlank(qo.getStartTime()) && StringUtils.isNotBlank(qo.getEndTime())) {
			wrapperX.between(SysUser::getCreateTime, qo.getStartTime(), qo.getEndTime());
		}
		this.selectByPage(page, wrapperX);
		return new PageResult<>(page.getRecords(), page.getTotal());
	}

	/**
	 * 批量更新用户状态
	 * @param userIds 用户ID集合
	 * @param status 状态
	 * @return 是否更新成功
	 */
	default boolean updateUserStatusBatch(List<Integer> userIds, Integer status) {
		int i = this.update(null,
				Wrappers.lambdaUpdate(SysUser.class).set(SysUser::getStatus, status).in(SysUser::getUserId, userIds));
		return SqlHelper.retBool(i);
	}

	/**
	 * 根据用户名查询用户
	 * @param username 用户名
	 * @return 系统用户
	 */
	default SysUser selectByUsername(String username) {
		return this.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
	}

	/**
	 * 更新指定用户的密码
	 * @param userId 用户
	 * @param password 密码
	 * @return 更新条数
	 */
	default boolean updatePassword(Integer userId, String password) {
		int i = this.update(null,
				Wrappers.<SysUser>lambdaUpdate().eq(SysUser::getUserId, userId).set(SysUser::getPassword, password));
		return SqlHelper.retBool(i);
	}

	/**
	 * 根据组织机构ID查询用户
	 * @param organizationIds 组织机构id集合
	 * @return 用户集合
	 */
	default List<SysUser> listByOrganizationIds(List<Integer> organizationIds) {
		return this.selectList(Wrappers.<SysUser>lambdaQuery().in(SysUser::getOrganizationId, organizationIds));
	}

	/**
	 * 根据用户类型查询用户
	 * @param userTypes 用户类型集合
	 * @return 用户集合
	 */
	default List<SysUser> listByUserTypes(List<Integer> userTypes) {
		return this.selectList(Wrappers.<SysUser>lambdaQuery().in(SysUser::getType, userTypes));
	}

	/**
	 * 根据用户Id集合查询用户
	 * @param userIds 用户Id集合
	 * @return 用户集合
	 */
	default List<SysUser> listByUserIds(List<Integer> userIds) {
		return this.selectList(Wrappers.<SysUser>lambdaQuery().in(SysUser::getUserId, userIds));
	}

	/**
	 * 根据RoleCode 查询对应用户
	 * @param roleCodes 角色标识
	 * @return List<SysUser> 该角色标识对应的用户列表
	 */
	List<SysUser> listByRoleCodes(@Param("roleCodes") List<String> roleCodes);

	/**
	 * 返回用户的select数据 name=> username value => userId
	 * @param userTypes 用户类型
	 * @return List<SelectData>
	 */
	List<SelectData<?>> listSelectData(@Param("userTypes") List<Integer> userTypes);

}
