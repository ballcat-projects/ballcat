package com.hccake.ballcat.system.event;

import com.hccake.ballcat.system.model.entity.SysUser;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * 用户创建事件
 *
 * @author Yakir
 */
@Getter
@ToString
public class UserCreatedEvent {

	private final SysUser sysUser;

	private final List<String> roleCodes;

	public UserCreatedEvent(SysUser sysUser, List<String> roleCodes) {
		this.sysUser = sysUser;
		this.roleCodes = roleCodes;
	}

}
