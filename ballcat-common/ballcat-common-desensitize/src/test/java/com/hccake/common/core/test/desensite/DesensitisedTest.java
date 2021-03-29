package com.hccake.common.core.test.desensite;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.ballcat.common.desensitize.json.JsonSerializerModifier;
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
		// 指定DesensitizeHandler 若ignore方法为true 则忽略脱敏 false 则启用脱敏
		JsonSerializerModifier modifier = new JsonSerializerModifier((fieldName) -> {
			log.info("当前字段名称{}", fieldName);
			return false;
		});
		// 不指定 实现类 默认使用脱敏规则
		// JsonSerializerModifier modifier = new JsonSerializerModifier();

		objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(modifier));
		DesensitizationUser user = new DesensitizationUser().setEmail("chengbohua@foxmail.com").setUsername("xiaoming")
				.setPassword("admina123456").setPhoneNumber("15800000000").setTestField("这是测试属性")
				.setCustomDesensitize("test");
		String value = objectMapper.writeValueAsString(user);

		Assert.isTrue(
				"{\"username\":\"xiaoming\",\"password\":\"adm****56\",\"email\":\"c****@foxmail.com\",\"phoneNumber\":\"158******00\",\"testField\":\"TEST-这是测试属性\",\"customDesensitize\":\"test\"}"
						.equals(value));

		log.info("脱敏后的数据：{}", value);
	}

}
