package com.hccake.starter.sms.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.hccake.starter.sms.SmsSender;
import com.hccake.starter.sms.SmsSenderParams;
import com.hccake.starter.sms.SmsSenderResult;
import com.hccake.starter.sms.constant.SmsSendConstants;
import com.hccake.starter.sms.enums.TypeEnum;
import com.hccake.starter.sms.exception.SmsException;
import com.hccake.starter.sms.impl.xinkuka.model.XinKuKaSend;
import com.hccake.starter.sms.impl.xinkuka.service.XinKuKaSmsSendService;
import com.hccake.starter.sms.properties.SmsProperties;
import com.hccake.starter.sms.properties.extra.XinKuKa;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @author lingting 2020/5/7 15:25
 */
@ConditionalOnProperty(name = "sms.type", havingValue = "XIN_KU_KA")
@RequiredArgsConstructor
public class XinKuKaSenderImpl extends BaseServiceImpl implements SmsSender<SmsSenderParams, SmsSenderResult> {

	private final SmsProperties properties;

	private final XinKuKaSmsSendService sendService;

	private XinKuKa xinKuKa = null;

	@Override
	public SmsSenderResult send(SmsSenderParams smsSenderParams) {
		if (xinKuKa == null) {
			xinKuKa = properties.getXinKuKa();
		}

		List<XinKuKaSend> list = new ArrayList<>();
		XinKuKaSend send;
		// 配置发送数据
		for (String p : smsSenderParams.getPhoneNumbers()) {
			send = new XinKuKaSend();
			// 指定发送短信
			send.setSmsType(SmsSendConstants.type.sms.getVal());

			// 指定号码发送
			if (xinKuKa.getMode() == XinKuKa.Mode.number) {
				send.setPhoNum(StrUtil.isEmpty(smsSenderParams.getUsePhone()) ? xinKuKa.getNumber()
						: smsSenderParams.getUsePhone());
			}
			send.setSmsNumber(p);
			// 不指定端口发送
			send.setPortNum(-1);
			send.setSmsContent(smsSenderParams.getContent());
			send.setSmsState(SmsSendConstants.state.notSend.getVal());
			list.add(send);
		}

		if (sendService.saveBatch(list)) {
			return SmsSenderResult.generateXinKuKa("数据已全部入库-当前时间：" + DateUtil.now(), smsSenderParams.toString(),
					smsSenderParams.getPhoneNumbers());
		}
		return errRet(TypeEnum.XIN_KU_KA, smsSenderParams.getPhoneNumbers(), "发送失败!", new SmsException("短信发送失败!"));
	}

}
