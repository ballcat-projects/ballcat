package com.hccake.ballcat.notify.model.domain;

import com.hccake.ballcat.notify.enums.NotifyChannelEnum;
import com.hccake.ballcat.notify.enums.NotifyRecipientFilterTypeEnum;

import java.util.List;

/**
 * @author Hccake 2020/12/23
 * @version 1.0
 */
public interface NotifyInfo {

	/**
	 * 标题
	 * @return String 当前通知标题
	 */
	String getTitle();

	/**
	 * 内容
	 * @return String 当前通知内容
	 */
	String getContent();

	/**
	 * 接收人筛选方式
	 * @see NotifyRecipientFilterTypeEnum
	 * @return Integer 接收人筛选方式
	 */
	Integer getRecipientFilterType();

	/**
	 * 对应接收人筛选方式的条件信息
	 * @return List<Object>
	 */
	List<Object> getRecipientFilterCondition();

	/**
	 * 接收方式，值与通知渠道一一对应
	 * @see NotifyChannelEnum
	 * @return List<Integer>
	 */
	List<Integer> getReceiveMode();

}
