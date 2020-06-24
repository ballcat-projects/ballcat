package com.hccake.ballcat.admin.modules.sys.model.converter;

import com.hccake.ballcat.admin.modules.sys.model.vo.Router;
import com.hccake.ballcat.admin.modules.sys.model.vo.PermissionVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/17 15:26
 */
@Mapper
public interface SysPermissionConverter {

	SysPermissionConverter INSTANCE = Mappers.getMapper(SysPermissionConverter.class);

	/**
	 * 转换permissionVO为Router
	 * @param permissionVO
	 * @return
	 */
	Router toRouter(PermissionVO permissionVO);

}
