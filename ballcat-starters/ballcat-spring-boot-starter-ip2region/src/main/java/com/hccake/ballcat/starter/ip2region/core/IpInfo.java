package com.hccake.ballcat.starter.ip2region.core;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

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

	// region 拼接完整的IP区域信息的几个方法

	/**
	 * 拼接完整的地址
	 * @return 描述IP区域信息的文本
	 */
	public String getAddress() {
		return getAddress("");
	}

	/**
	 * 拼接完整的地址
	 * @param delimiter 拼接用的分隔符
	 * @return 描述IP区域信息的文本
	 */
	public String getAddress(String delimiter) {
		Set<String> regionSet = new LinkedHashSet<>();
		regionSet.add(country);
		regionSet.add(province);
		regionSet.add(city);
		regionSet.add(area);
		regionSet.removeIf(Objects::isNull);
		return StringUtils.collectionToDelimitedString(regionSet, delimiter);
	}

	/**
	 * 拼接完整的地址(带服务提供商)
	 * @return 描述IP区域信息的文本
	 */
	public String getAddressAndIsp() {
		return getAddressAndIsp("");
	}

	/**
	 * 拼接完整的地址(带服务提供商)
	 * @param delimiter 拼接用的分隔符
	 * @return 描述IP区域信息的文本
	 */
	public String getAddressAndIsp(String delimiter) {
		Set<String> regionSet = new LinkedHashSet<>();
		regionSet.add(country);
		regionSet.add(province);
		regionSet.add(city);
		regionSet.add(area);
		regionSet.add(isp);
		regionSet.removeIf(Objects::isNull);
		return StringUtils.collectionToDelimitedString(regionSet, delimiter);
	}

	// endregion

}
