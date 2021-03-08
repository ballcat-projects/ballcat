package com.hccake.common.core.test.desensite;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Hccake 2021/1/23
 * @version 1.0
 */
@Slf4j
class DesensitisedTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void test1() throws JsonProcessingException {
		DesensitizationUser user = new DesensitizationUser().setEmail("chengbohua@foxmail.com").setUsername("xiaoming")
				.setPassword("admina123456").setPhoneNumber("15800000000").setTestField("这是测试属性");
		String value = objectMapper.writeValueAsString(user);
		Assert.isTrue(
				"{\"username\":\"xiaoming\",\"password\":\"adm****56\",\"email\":\"c****@foxmail.com\",\"phoneNumber\":\"158******00\",\"testField\":\"TEST-这是测试属性\"}"
						.equals(value));

		log.info("脱敏后的数据：{}", value);
	}

}
