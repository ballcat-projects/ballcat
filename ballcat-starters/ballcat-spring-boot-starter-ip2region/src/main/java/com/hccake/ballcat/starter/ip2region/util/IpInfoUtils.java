package com.hccake.ballcat.starter.ip2region.util;

import com.hccake.ballcat.starter.ip2region.core.IpInfo;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * ip 信息详情
 *
 * @author lishangbu 2022/10/16
 */
public final class IpInfoUtils {

	private IpInfoUtils() {
	}

	/**
	 * 缓存正则表达式，提升编译速度
	 */
	private static final Pattern SPLIT_PATTERN = Pattern.compile("\\|");

	/**
	 * ip2Region 采用 0 填充的没有数据的字段
	 */
	private static final String NO_DATA = "0";

	/**
	 * 将 DataBlock 转化为 IpInfo
	 * @param dataBlock 数据块,格式为<code>国家|区域|省份|城市|ISP</code>
	 * @return IpInfo
	 */
	@Nullable
	public static IpInfo toIpInfo(@Nullable String dataBlock) {
		return toIpInfo(null, dataBlock);
	}

	/**
	 * 将 DataBlock 转化为 IpInfo
	 * @param originIp 原始IP信息
	 * @param dataBlock 数据块,格式为<code>国家|区域|省份|城市|ISP</code>
	 * @return IpInfo
	 */
	@Nullable
	public static IpInfo toIpInfo(@Nullable String originIp, @Nullable String dataBlock) {
		if (dataBlock == null) {
			return null;
		}
		IpInfo ipInfo = new IpInfo(originIp);
		String[] tmp = SPLIT_PATTERN.split(dataBlock);
		// 补齐5位
		if (tmp.length < 5) {
			tmp = Arrays.copyOf(tmp, 5);
		}
		ipInfo.setCountry(filterZero(tmp[0]));
		ipInfo.setArea(filterZero(tmp[1]));
		ipInfo.setProvince(filterZero(tmp[2]));
		ipInfo.setCity(filterZero(tmp[3]));
		ipInfo.setIsp(filterZero(tmp[4]));
		return ipInfo;
	}

	/**
	 * 数据过滤，因为 ip2Region 采用 0 填充的没有数据的字段
	 * @param info info
	 * @return info
	 */
	@Nullable
	private static String filterZero(@Nullable String info) {
		// null 或 0 返回 null
		if (info == null || NO_DATA.equals(info)) {
			return null;
		}
		return info;
	}

}