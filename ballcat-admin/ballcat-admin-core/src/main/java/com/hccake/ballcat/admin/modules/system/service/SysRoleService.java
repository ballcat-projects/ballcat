package com.hccake.ballcat.admin.modules.system.service;

import com.hccake.ballcat.admin.modules.system.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.system.model.qo.SysRoleQO;
import com.hccake.ballcat.admin.modules.system.model.vo.SysRolePageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.domain.SelectData;
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
	PageResult<SysRolePageVO> queryPage(PageParam pageParam, SysRoleQO qo);

	/**
	 * 角色的选择数据
	 * @return 角色下拉列表数据集合
	 */
	List<SelectData<?>> listSelectData();

}
