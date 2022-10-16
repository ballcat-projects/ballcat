package com.hccake.ballcat.starter.ip2region.searcher;

import com.hccake.ballcat.starter.ip2region.config.Ip2regionProperties;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

import java.io.InputStream;

/**
 *
 * 在内存中缓存整个IP数据库实现检索
 *
 * <p>
 * 可以预先加载整个 ip2region.xdb 的数据到内存，然后基于这个数据创建查询对象来实现完全基于文件的查询，类似之前的 memory search
 * </p>
 *
 * <p>
 * 参考<a href=
 * "https://gitee.com/lionsoul/ip2region/tree/master/binding/java">ip2region</a>魔改
 * </p>
 *
 * @author lishangbu
 * @date 2022/10/16
 */
public class CacheXdbFileIp2regionSearcher extends Ip2regionSearcherTemplate {

	public CacheXdbFileIp2regionSearcher(ResourceLoader resourceLoader, Ip2regionProperties properties) {
		super(resourceLoader, properties);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Resource resource = resourceLoader.getResource(properties.getFileLocation());
		try (InputStream inputStream = resource.getInputStream()) {
			this.searcher = Searcher.newWithBuffer(StreamUtils.copyToByteArray(inputStream));
		}
	}

}
