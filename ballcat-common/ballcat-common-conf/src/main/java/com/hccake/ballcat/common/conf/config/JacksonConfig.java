package com.hccake.ballcat.common.conf.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.ballcat.common.core.jackson.ArraySerializerModifier;
import com.hccake.ballcat.common.core.jackson.JavaTimeModule;
import com.hccake.ballcat.common.core.jackson.NullSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 22:14
 */
@Configuration
@ConditionalOnClass(ObjectMapper.class)
public class JacksonConfig {

    /**
     * 自定义objectMapper
     * @return ObjectMapper
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();


        // NULL 数组转 []
        objectMapper.setSerializerFactory(objectMapper.getSerializerFactory()
                .withSerializerModifier(new ArraySerializerModifier()));

        // NULL 值转 ""
        objectMapper.getSerializerProvider().setNullValueSerializer(new NullSerializer());

        // 所有数字转字符串输出
        // objectMapper.configure(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS.mappedFeature(), true);

        //注册xss解析器
//        SimpleModule xssModule = new SimpleModule("XssStringJsonSerializer", PackageVersion.VERSION);
//        xssModule.addSerializer(new XssStringJsonSerializer());
//        objectMapper.registerModule(xssModule);

        // 时间解析器
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }

}

