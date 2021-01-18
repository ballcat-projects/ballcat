package com.hccake.ballcat.admin.modules.sys.service;

import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysRoleQO;
import com.hccake.ballcat.admin.modules.sys.model.vo.SysRoleVO;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.domain.PageResult;
import com.hccake.ballcat.common.core.domain.SelectData;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.List;

/**
 * <p>
 * 系统角色服务类
 * </p>
 *
 * @author hccake
 * @since 2020-01-12
 */
public interface SysRoleService extends ExtendService<SysRole> {

	/**
	 * 查询系统角色列表
	 * @param pageParam 分页参数
	 * @param qo 查询参数
	 * @return 分页对象
	 */
	PageResult<SysRoleVO> queryPage(PageParam pageParam, SysRoleQO qo);

	/**
	 * 通过角色ID，删除角色
	 * @param id 角色ID
	 * @return 删除是否成功
	 */
	Boolean removeRoleById(Integer id);

	/**
	 * 角色的选择数据
	 * @return 角色下拉列表数据集合
	 */
	List<SelectData<?>> getSelectData();

}
