package org.ballcat.sms.tencent;

import lombok.Getter;
import lombok.Setter;
import org.ballcat.sms.properties.SmsProperties;
import org.ballcat.sms.properties.extra.Tencent;
import org.ballcat.sms.sender.AbstractSenderParams;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lingting 2023-10-16 19:45
 */
@Getter
@Setter
public class TencentSenderParams extends AbstractSenderParams<TencentSenderParams> {

	/**
	 * 短信签名, 未指定使用 {@link SmsProperties#getTencent()#getSign()}
	 */
	private String sign;

	/**
	 * 短信模板ID, 如果未指定则使用 {@link SmsProperties#getTencent()#getTemplateId()}
	 */
	private Integer templateId;

	/**
	 * 短信模板参数
	 *
	 * @see Tencent
	 */
	private List<Object> templateParam = new ArrayList<>();

	/**
	 * 添加模板参数
	 */
	public TencentSenderParams addTemplateParam(Object param) {
		this.templateParam.add(param);
		return this;
	}

}
