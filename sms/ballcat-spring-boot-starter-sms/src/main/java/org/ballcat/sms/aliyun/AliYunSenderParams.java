package org.ballcat.sms.aliyun;

import lombok.Getter;
import lombok.Setter;
import org.ballcat.sms.properties.SmsProperties;
import org.ballcat.sms.sender.AbstractSenderParams;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lingting 2023-10-16 19:31
 */
@Getter
@Setter
public class AliYunSenderParams extends AbstractSenderParams<AliYunSenderParams> {

	/**
	 * 短信签名名称, 未指定使用 {@link SmsProperties#getAliyun()#getSignName()}
	 */
	private String signName;

	/**
	 * 短信模板ID, 如果未指定则使用 {@link SmsProperties#getAliyun()#getTemplateId()}
	 */
	private String templateId;

	/**
	 * 短信模板参数
	 */
	private Map<String, Object> aliyunTemplateParam = new HashMap<>();

}
