package com.hccake.ballcat.admin.modules.system.event;

import com.hccake.ballcat.admin.modules.system.model.entity.SysUser;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 用户修改事件
 *
 * @author Yakir
 */
@Getter
public class UserChangeEvent extends ApplicationEvent {

	private final SysUser sysUser;

	public UserChangeEvent(SysUser sysUser) {
		super(sysUser);
		this.sysUser = sysUser;
	}

}
