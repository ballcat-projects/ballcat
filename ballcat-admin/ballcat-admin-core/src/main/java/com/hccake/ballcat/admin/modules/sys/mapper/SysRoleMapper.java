package com.hccake.ballcat.admin.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysRole;
import com.hccake.ballcat.common.core.vo.SelectData;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author ballcat
 * @since 2017-10-29
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

	/**
	 * 获取角色下拉框数据
	 * @return
	 */
	List<SelectData<?>> getSelectData();

}
