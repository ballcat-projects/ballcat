package com.hccake.starter.sms.impl;

import cn.hutool.core.util.StrUtil;
import com.hccake.starter.sms.SmsSenderResult;
import com.hccake.starter.sms.enums.TypeEnum;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lingting 2020/4/26 13:45
 */
@Slf4j
public class BaseServiceImpl {

	/**
	 * b 异常返回处理
	 */
	public SmsSenderResult errRet(TypeEnum platform, Set<String> phoneNumbers, String msg, Exception e) {
		String id = StrUtil.uuid();
		log.error(msg + "\nerror_id: " + id, e);
		return SmsSenderResult.generateException(platform, phoneNumbers, id, e);
	}

}
