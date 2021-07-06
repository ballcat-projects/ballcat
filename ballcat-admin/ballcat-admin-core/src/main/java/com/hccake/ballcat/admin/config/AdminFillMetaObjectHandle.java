package com.hccake.ballcat.admin.config;

import com.hccake.ballcat.common.conf.mybatis.FillMetaObjectHandle;
import com.hccake.ballcat.common.security.userdetails.User;
import com.hccake.ballcat.common.security.util.SecurityUtils;
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

		User user = SecurityUtils.getUser();
		if (user != null) {
			this.strictInsertFill(metaObject, "createBy", Integer.class, user.getUserId());
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		super.updateFill(metaObject);

		User user = SecurityUtils.getUser();
		if (user != null) {
			this.strictUpdateFill(metaObject, "updateBy", Integer.class, user.getUserId());
		}
	}

}
