package com.hccake.ballcat.admin.modules.notify.recipient;

import com.hccake.ballcat.admin.constants.NotifyRecipientFilterType;
import com.hccake.ballcat.admin.modules.system.model.entity.SysUser;
import com.hccake.ballcat.admin.modules.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hccake 2020/12/21
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class SpecifyUserTypeRecipientFilter implements RecipientFilter {

	private final SysUserService sysUserService;

	/**
	 * 当前筛选器对应的筛选类型
	 * @return 筛选类型对应的标识
	 * @see NotifyRecipientFilterType
	 */
	@Override
	public Integer filterType() {
		return NotifyRecipientFilterType.SPECIFY_USER_TYPE.getValue();
	}

	/**
	 * 接收者筛选
	 * @param filterCondition 筛选条件
	 * @return 接收者集合
	 */
	@Override
	public List<SysUser> filter(List<Object> filterCondition) {
		List<Integer> userTypes = filterCondition.stream().map(Integer.class::cast).collect(Collectors.toList());
		return sysUserService.listByUserTypes(userTypes);
	}

	/**
	 * 获取当前用户的过滤属性
	 * @param sysUser 系统用户
	 * @return 该用户所对应筛选条件的属性
	 */
	@Override
	public Object getFilterAttr(SysUser sysUser) {
		return sysUser.getType();
	}

	/**
	 * 是否匹配当前用户
	 * @param filterAttr 筛选属性
	 * @param filterCondition 筛选条件
	 * @return boolean true: 是否匹配
	 */
	@Override
	public boolean match(Object filterAttr, List<Object> filterCondition) {
		Integer type = (Integer) filterAttr;
		return filterCondition.stream().map(Integer.class::cast).anyMatch(x -> x.equals(type));
	}

}
