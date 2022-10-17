package com.hccake.ballcat.starter.ip2region.core;

import lombok.Data;

import java.io.Serializable;

/**
 * IP信息
 *
 * @author lishangbu
 * @date 2022/10/16
 */
@Data
public class IpInfo implements Serializable {

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 省
	 */
	private String province;

	/**
	 * 城市
	 */
	private String city;

	/**
	 * 区域
	 */
	private String area;

	/**
	 * 运营商
	 */
	private String isp;

	/**
	 * 原始IP
	 */
	private String originIp;

	/**
	 * No args constructor
	 */
	public IpInfo() {
	}

	/**
	 * <code>originIp</code> arg constructor
	 * @param originIp
	 */
	public IpInfo(String originIp) {
		this.originIp = originIp;
	}

}
