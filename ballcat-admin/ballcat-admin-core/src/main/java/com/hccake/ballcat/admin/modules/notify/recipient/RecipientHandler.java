package com.hccake.ballcat.admin.modules.notify.recipient;

import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

}
