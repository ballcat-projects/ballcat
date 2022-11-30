package com.hccake.ballcat.starter.ip2region.searcher;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

/**
 * index缓存测试查询IP
 *
 * @author lishangbu
 * @since 2022/10/16
 */
@ActiveProfiles("index")
public class IndexCacheIp2RegionSearcherTest extends Ip2regionSearcherTest {

	@Test
	public void testIpSearch() {
		super.testIpSearch();
	}

}
