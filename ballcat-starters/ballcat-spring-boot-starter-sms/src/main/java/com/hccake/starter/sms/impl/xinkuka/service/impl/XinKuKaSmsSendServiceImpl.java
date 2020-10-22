package com.hccake.starter.sms.impl.xinkuka.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.starter.sms.impl.xinkuka.mapper.SmsSendMapper;
import com.hccake.starter.sms.impl.xinkuka.model.XinKuKaSend;
import com.hccake.starter.sms.impl.xinkuka.service.XinKuKaSmsSendService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author lingting 2020/5/7 16:52
 */
@Service
@ConditionalOnProperty(name = "sms.type", havingValue = "XIN_KU_KA")
public class XinKuKaSmsSendServiceImpl extends ServiceImpl<SmsSendMapper, XinKuKaSend>
		implements XinKuKaSmsSendService {

}
