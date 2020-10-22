package com.hccake.starter.sms.properties.extra;

import lombok.Data;

/**
 * @author lingting 2020/5/6 17:16
 */
@Data
public class Tencent {

	/**
	 * 调用api参数所需的 sdk ip
	 */
	private int sdkId;

	/**
	 * 配置节点
	 */
	private String endpoint = "sms.tencentcloudapi.com";

	/**
	 * 分区
	 */
	private String region = "";

	/**
	 * 短信模板id
	 */
	private Integer templateId = null;

	/**
	 * 短信签名，发往国内必填
	 */
	private String sign = null;

}
