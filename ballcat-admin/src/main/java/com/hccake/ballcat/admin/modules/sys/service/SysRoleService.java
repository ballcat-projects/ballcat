package com.hccake.ballcat.admin.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.common.core.vo.SelectData;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author
 * @since 2017-10-29
 */
public interface SysRoleService extends IService<SysRole> {


	/**
	 * 通过角色ID，删除角色
	 *
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
