package com.hccake.common.core.test.desensite.custom;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.ballcat.common.desensitize.AnnotationHandlerHolder;
import com.hccake.ballcat.common.desensitize.DesensitizationHandlerHolder;
import com.hccake.ballcat.common.desensitize.functions.DesensitizeFunction;
import com.hccake.ballcat.common.desensitize.json.JsonSerializerModifier;
import com.hccake.common.core.test.desensite.DesensitizationUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.lang.annotation.Annotation;

/**
 * @author Hccake 2021/1/23
 * @version 1.0
 */
@Slf4j
class CustomDesensitisedTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeTestMethod
	void  beforeInit(){
		//注册自定义脱敏类型处理器
		CustomDesensitisedHandler customDesensitisedHandler = new CustomDesensitisedHandler();
		DesensitizationHandlerHolder.addHandler(CustomDesensitisedHandler.class,customDesensitisedHandler);
		//注册注解 处理器
		AnnotationHandlerHolder.addHandleFunction(CustomerDesensitize.class, new DesensitizeFunction() {
			@Override
			public String desensitize(Annotation annotation, String value) {
				CustomerDesensitize customerDesensitize= (CustomerDesensitize) annotation;
				return null;
			}
		});
	}

	@Test
	void desensitizedExtend() throws JsonProcessingException {
		// 初始化序列号modifier
		JsonSerializerModifier modifier = new JsonSerializerModifier();
		objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(modifier));
		// 注册我们自定义的脱敏处理
		AnnotationHandlerHolder.addHandleFunction(CustomerDesensitize.class, ((annotation, value) -> {
			CustomerDesensitize an = (CustomerDesensitize) annotation;
           return "";
		}));

		DesensitizationUser user = new DesensitizationUser().setEmail("chengbohua@foxmail.com").setUsername("xiaoming")
				.setPassword("admina123456").setPhoneNumber("15800000000").setTestField("这是测试属性")
				.setCustomDesensitize("自定义属性");
		String value = objectMapper.writeValueAsString(user);
        Assert.isTrue(
        		"{\"username\":\"xiaoming\",\"password\":\"adm****56\",\"email\":\"c****@foxmail.com\",\"phoneNumber\":\"158******00\",\"testField\":\"TEST-这是测试属性\",\"customDesensitize\":\"TEST-自定义属性\"}"
		.equals(value));
		log.info("脱敏后的数据：{}", value);
	}

}
