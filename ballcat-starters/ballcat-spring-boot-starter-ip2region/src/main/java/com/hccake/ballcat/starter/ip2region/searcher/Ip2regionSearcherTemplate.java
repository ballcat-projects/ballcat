package com.hccake.ballcat.starter.ip2region.searcher;

import com.hccake.ballcat.starter.ip2region.config.Ip2regionProperties;
import com.hccake.ballcat.starter.ip2region.core.IpInfo;
import com.hccake.ballcat.starter.ip2region.util.IpInfoUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

/**
 * Ip2region 搜索服务实现
 *
 * @author lishangbu
 * @date 2022/10/16
 */
@RequiredArgsConstructor
public abstract class Ip2regionSearcherTemplate implements DisposableBean, InitializingBean, Ip2regionSearcher {

	protected final ResourceLoader resourceLoader;

	protected final Ip2regionProperties properties;

	protected Searcher searcher;

	@Override
	@SneakyThrows(value = IOException.class)
	public IpInfo search(long ip) {
		return IpInfoUtils.toIpInfo(Searcher.long2ip(ip), searcher.search(ip));

	}

	@Override
	@SneakyThrows(value = Exception.class)
	public IpInfo search(String ip) {
		return IpInfoUtils.toIpInfo(ip, searcher.search(ip));
	}

	@Override
	public IpInfo searchQuietly(long ip) {
		try {
			return search(ip);
		}
		catch (Exception e) {
			// do nothing
			return null;
		}
	}

	@Override
	public IpInfo searchQuietly(String ip) {
		try {
			return search(ip);
		}
		catch (Exception e) {
			// do nothing
			return null;
		}
	}

	@Override
	public void destroy() throws Exception {
		if (this.searcher != null) {
			this.searcher.close();
		}
	}

}
