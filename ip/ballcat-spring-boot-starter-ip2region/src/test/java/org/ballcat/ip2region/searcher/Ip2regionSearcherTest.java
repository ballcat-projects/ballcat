/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.ip2region.searcher;

import org.ballcat.autoconfigure.ip2region.Ip2regionAutoConfiguration;
import org.ballcat.ip2region.core.IpInfo;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 测试
 *
 * @author lishangbu
 * @since 2022/10/16
 */
@SpringBootTest(classes = Ip2regionAutoConfiguration.class)
public abstract class Ip2regionSearcherTest {

	@Autowired
	protected Ip2regionSearcher ip2regionSearcher;

	protected void testIpSearch() {
		IpInfo localIp = this.ip2regionSearcher.search("127.0.0.1");
		Assertions.assertNull(localIp.getCountry());
		Assertions.assertNull(localIp.getProvince());
		Assertions.assertNull(localIp.getArea());
		Assertions.assertEquals("内网IP", localIp.getCity());
		Assertions.assertEquals("内网IP", localIp.getIsp());
		Assertions.assertEquals("127.0.0.1", localIp.getOriginIp());
		Assertions.assertEquals("内网IP", localIp.getAddress());
		Assertions.assertEquals("内网IP", localIp.getAddress(","));
		Assertions.assertEquals("内网IP", localIp.getAddressAndIsp());
		Assertions.assertEquals("内网IP", localIp.getAddressAndIsp(","));

		IpInfo remoteIp = this.ip2regionSearcher.search("39.188.108.178");
		Assertions.assertEquals("中国", remoteIp.getCountry());
		Assertions.assertEquals("浙江省", remoteIp.getProvince());
		Assertions.assertNull(localIp.getArea());
		Assertions.assertEquals("宁波市", remoteIp.getCity());
		Assertions.assertEquals("移动", remoteIp.getIsp());
		Assertions.assertEquals("39.188.108.178", remoteIp.getOriginIp());
		Assertions.assertEquals("中国浙江省宁波市", remoteIp.getAddress());
		Assertions.assertEquals("中国 浙江省 宁波市", remoteIp.getAddress(" "));
		Assertions.assertEquals("中国浙江省宁波市移动", remoteIp.getAddressAndIsp());
		Assertions.assertEquals("中国 浙江省 宁波市 移动", remoteIp.getAddressAndIsp(" "));

		String errorIp = "BallCat.Is.N.B";
		Assertions.assertNull(this.ip2regionSearcher.searchQuietly(errorIp));
		Assertions.assertThrowsExactly(NumberFormatException.class, () -> this.ip2regionSearcher.search(errorIp));

	}

}
