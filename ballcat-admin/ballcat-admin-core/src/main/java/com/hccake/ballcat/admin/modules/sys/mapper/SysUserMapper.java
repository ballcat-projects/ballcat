package com.hccake.ballcat.admin.modules.sys.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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
	 * 根据RoleCode 查询对应用户
	 * @param roleCodes 角色标识
	 * @return List<SysUser> 该角色标识对应的用户列表
	 */
	List<SysUser> selectUsersByRoleCodes(@Param("roleCodes") List<String> roleCodes);

	/**
	 * 返回用户的select数据 name=> username value => userId
	 * @param userTypes 用户类型
	 * @return List<SelectData>
	 */
	List<SelectData<?>> getSelectData(@Param("userTypes") List<Integer> userTypes);

}
