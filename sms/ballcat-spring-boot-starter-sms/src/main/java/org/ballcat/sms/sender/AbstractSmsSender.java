package org.ballcat.sms.sender;

import org.ballcat.sms.SmsSenderResult;
import org.ballcat.sms.enums.TypeEnum;
import org.bson.types.ObjectId;
import org.slf4j.Logger;

import java.util.Set;

/**
 * 短信发送 接口类
 *
 * @author lingting 2020/4/26 9:55
 */
@SuppressWarnings("java:S112")
public abstract class AbstractSmsSender<P extends AbstractSenderParams<P>> {

	protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	/**
	 * 发送短信
	 * @param p 参数配置
	 * @return 短信发送结果
	 */
	public SmsSenderResult send(P p) {
		TypeEnum platform = platform();
		try {
			return doSend(p);
		}
		catch (Exception e) {
			String msg = platform.name() + "平台发送短信出现异常!";
			return errRet(platform, p.getPhoneNumbers(), msg, e);
		}
	}

	/**
	 * 异常返回处理
	 */
	public SmsSenderResult errRet(TypeEnum platform, Set<String> phoneNumbers, String msg, Exception e) {
		String id = ObjectId.get().toString();
		log.error("sms result error! errorId: {}; {}", id, msg, e);
		return SmsSenderResult.generateException(platform, phoneNumbers, id, e);
	}

	public abstract TypeEnum platform();

	protected abstract SmsSenderResult doSend(P p) throws Exception;

}
