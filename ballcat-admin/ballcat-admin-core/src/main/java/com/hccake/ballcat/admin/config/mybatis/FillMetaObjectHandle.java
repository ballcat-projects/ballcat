package com.hccake.ballcat.admin.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.hccake.ballcat.common.core.constant.GlobalConstants;
import com.hccake.ballcat.common.security.userdetails.User;
import com.hccake.ballcat.common.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/7/26 14:41
 */
@Slf4j
public class FillMetaObjectHandle implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		// 逻辑删除标识
		this.strictInsertFill(metaObject, "deleted", Long.class, GlobalConstants.NOT_DELETED_FLAG);
		// 创建时间
		this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
		// 创建人
		User user = SecurityUtils.getUser();
		if (user != null) {
			this.strictInsertFill(metaObject, "createBy", Integer.class, user.getUserId());
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		// 修改时间
		this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
		// 修改人
		User user = SecurityUtils.getUser();
		if (user != null) {
			this.strictUpdateFill(metaObject, "updateBy", Integer.class, user.getUserId());
		}
	}

}
