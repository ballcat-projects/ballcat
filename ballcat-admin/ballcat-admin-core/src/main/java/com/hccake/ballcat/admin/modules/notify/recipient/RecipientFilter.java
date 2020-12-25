package com.hccake.ballcat.admin.modules.notify.recipient;

import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;

import java.util.List;

/**
 * 接收者筛选器
 *
 * @author Hccake 2020/12/21
 * @version 1.0
 */
public interface RecipientFilter {

	/**
	 * 当前筛选器对应的筛选类型
	 * @see com.hccake.ballcat.admin.constants.NotifyRecipientFilterType
	 * @return 筛选类型对应的标识
	 */
	Integer filterType();

	/**
	 * 接收者筛选
	 * @param filterCondition 筛选条件
	 * @return 接收者集合
	 */
	List<SysUser> filter(List<Object> filterCondition);

	/**
	 * 获取当前用户的过滤属性
	 * @param sysUser 系统用户
	 * @return 该用户所对应筛选条件的属性
	 */
	Object getFilterAttr(SysUser sysUser);

	/**
	 * 是否匹配当前用户
	 * @param filterAttr 筛选属性
	 * @param filterCondition 筛选条件
	 * @return boolean true: 是否匹配
	 */
	boolean match(Object filterAttr, List<Object> filterCondition);

}
