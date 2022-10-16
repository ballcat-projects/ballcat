package com.hccake.ballcat.starter.ip2region.searcher;

import com.hccake.ballcat.starter.ip2region.config.Ip2regionProperties;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 *
 * 缓存索引数据实现检索
 *
 * <p>
 * 提前从 xdb 文件中加载出来 VectorIndex 数据，然后全局缓存，每次创建 Searcher 对象的时候使用全局的 VectorIndex 缓存可以减少一次固定的
 * IO 操作，从而加速查询，减少 IO 压力
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
public class CacheVectorIndexIp2regionSearcher extends Ip2regionSearcherTemplate {

	public CacheVectorIndexIp2regionSearcher(ResourceLoader resourceLoader, Ip2regionProperties properties) {
		super(resourceLoader, properties);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Resource resource = resourceLoader.getResource(properties.getFileLocation());
		String dbPath = resource.getFile().getPath();
		this.searcher = Searcher.newWithVectorIndex(dbPath, Searcher.loadVectorIndexFromFile(dbPath));
	}

}
