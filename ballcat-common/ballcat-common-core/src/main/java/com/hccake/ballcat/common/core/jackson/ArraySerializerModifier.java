package com.hccake.ballcat.common.core.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 23:19
 */
public class ArraySerializerModifier extends BeanSerializerModifier {
    private final JsonSerializer<Object> nullArrayJsonSerializer = new NullArrayJsonSerializer();
    private final JsonSerializer<Object> nullMapJsonSerializer = new NullMapJsonSerializer();


    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        // 循环所有的beanPropertyWriter
		for (BeanPropertyWriter writer : beanProperties) {
			// 判断字段的类型，如果是array，list，set则注册nullSerializer
			if (isArrayType(writer)) {
				writer.assignNullSerializer(this.nullArrayJsonSerializer);
			} else if (isMapType(writer)) {
				writer.assignNullSerializer(this.nullMapJsonSerializer);
			}
		}
        return beanProperties;
    }

    /**
     * 是否是Map类型
     * @param writer BeanPropertyWriter
     * @return boolean
     */
    private boolean isMapType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return Map.class.isAssignableFrom(clazz);
    }

    /**
     * 是否是集合类型
     * @param writer BeanPropertyWriter
     * @return boolean
     */
    private boolean isArrayType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return clazz.isArray() || Collection.class.isAssignableFrom(clazz);

    }

}
