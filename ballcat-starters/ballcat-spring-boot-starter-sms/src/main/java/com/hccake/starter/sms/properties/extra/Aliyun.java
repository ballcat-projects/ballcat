package com.hccake.starter.sms.properties.extra;

import lombok.Data;

/**
 * @author 疯狂的狮子Li 2022-04-21
 */
@Data
public class Aliyun {

	/**
	 * 配置节点
	 */
	private String endpoint = "dysmsapi.aliyuncs.com";

	private String accessKeyId;

	private String accessKeySecret;

	/*
	 * 短信签名名称
	 */
	private String signName;

	/**
	 * 短信模板ID
	 */
	private String templateId;

}
