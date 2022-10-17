package com.hccake.ballcat.starter.ip2region.searcher;

import com.hccake.ballcat.starter.ip2region.config.Ip2regionProperties;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 *
 * 不缓存,直接检索
 *
 * <p>
 * 完全基于文件的查询
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
public class NoneCacheIp2regionSearcher extends Ip2regionSearcherTemplate {

	public NoneCacheIp2regionSearcher(ResourceLoader resourceLoader, Ip2regionProperties properties) {
		super(resourceLoader, properties);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Resource resource = resourceLoader.getResource(properties.getFileLocation());
		searcher = Searcher.newWithFileOnly(resource.getFile().getPath());
	}

}
