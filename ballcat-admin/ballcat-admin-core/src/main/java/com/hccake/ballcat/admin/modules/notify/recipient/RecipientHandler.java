package com.hccake.ballcat.admin.modules.notify.recipient;

import com.hccake.ballcat.system.model.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Hccake 2020/12/21
 * @version 1.0
 */
@Slf4j
@Component
public class RecipientHandler {

	private final Map<Integer, RecipientFilter> recipientFilterMap = new LinkedHashMap<>();

	public RecipientHandler(List<RecipientFilter> recipientFilterList) {
		for (RecipientFilter recipientFilter : recipientFilterList) {
			this.recipientFilterMap.put(recipientFilter.filterType(), recipientFilter);
		}
	}

	public List<SysUser> query(Integer filterType, List<Object> filterCondition) {
		RecipientFilter recipientFilter = recipientFilterMap.get(filterType);
		if (recipientFilter == null) {
			log.error("Unknown recipient filter：[{}]，filterCondition：{}", filterType, filterCondition);
			return new ArrayList<>();
		}
		return recipientFilter.filter(filterCondition);
	}

	/**
	 * 判断当前是否匹配
	 * @param recipientFilterType 筛选类型
	 * @param filterAttr 筛选属性
	 * @param recipientFilterCondition 筛选条件
	 * @return boolean true：匹配
	 */
	public boolean match(Integer recipientFilterType, Object filterAttr, List<Object> recipientFilterCondition) {
		RecipientFilter recipientFilter = recipientFilterMap.get(recipientFilterType);
		return recipientFilter != null && recipientFilter.match(filterAttr, recipientFilterCondition);
	}

	/**
	 * 获取当前用户的各个筛选器对应的属性
	 * @param sysUser 系统用户
	 * @return 属性Map key：filterType value: attr
	 */
	public Map<Integer, Object> getFilterAttrs(SysUser sysUser) {
		Map<Integer, Object> map = new HashMap<>(recipientFilterMap.size());
		for (RecipientFilter recipientFilter : recipientFilterMap.values()) {
			Object obj = recipientFilter.getFilterAttr(sysUser);
			map.put(recipientFilter.filterType(), obj);
		}
		return map;
	}

}
