package com.hccake.ballcat.admin.modules.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.admin.modules.sys.model.qo.SysRoleQO;
import com.hccake.ballcat.common.core.vo.SelectData;

import java.util.List;

/**
 * <p>
 * 系统角色服务类
 * </p>
 *
 * @author hccake
 * @since 2020-01-12
 */
public interface SysRoleService extends IService<SysRole> {

	/**
	 * 查询系统角色列表
	 * @param page 分页对象
	 * @param qo 查询参数
	 * @return 分页对象
	 */
	IPage<SysRole> page(IPage<SysRole> page, SysRoleQO qo);

	/**
	 * 通过角色ID，删除角色
	 * @param id
	 * @return
	 */
	Boolean removeRoleById(Integer id);

	/**
	 * 角色的选择数据
	 * @return
	 */
	List<SelectData> getSelectData();

}
