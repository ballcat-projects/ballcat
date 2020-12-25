package com.hccake.ballcat.admin.config;

import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.admin.oauth.util.SecurityUtils;
import com.hccake.ballcat.common.conf.mybatis.FillMetaObjectHandle;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/7/26 14:41 admin 模块自动填充
 */
@Slf4j
public class AdminFillMetaObjectHandle extends FillMetaObjectHandle {

	@Override
	public void insertFill(MetaObject metaObject) {
		super.insertFill(metaObject);

		SysUser sysUser = SecurityUtils.getSysUser();
		if (sysUser != null) {
			this.strictInsertFill(metaObject, "createBy", Integer.class, sysUser.getUserId());
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		super.updateFill(metaObject);

		SysUser sysUser = SecurityUtils.getSysUser();
		if (sysUser != null) {
			this.strictUpdateFill(metaObject, "updateBy", Integer.class, sysUser.getUserId());
		}
	}

}
