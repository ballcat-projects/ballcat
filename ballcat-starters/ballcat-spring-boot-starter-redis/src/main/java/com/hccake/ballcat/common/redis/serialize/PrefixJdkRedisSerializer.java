package com.hccake.ballcat.common.redis.serialize;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/3/27 22:57
 * 自定义Key序列化工具，添加全局key前缀
 */
@Slf4j
public class PrefixJdkRedisSerializer extends JdkSerializationRedisSerializer {
    private final String prefix;
    private final boolean enable;

    public PrefixJdkRedisSerializer(String prefix) {
        this.prefix = prefix;
        this.enable = prefix != null && !"".equals(prefix);
    }

    @Override
    public Object deserialize(byte[] bytes) {
        Object origin = super.deserialize(bytes);
        if (enable && origin instanceof String) {
            String originKey = (String) origin;
            // 如果有全局前缀，则需要删除
            if (originKey.startsWith(prefix)) {
                originKey = originKey.substring(prefix.length());
            }
            return originKey;
        }else {
            return origin;
        }
    }

    @Override
    public byte[] serialize(Object object) {
        if (enable && object instanceof String) {
            String key = prefix + object;
            return super.serialize(key);
        } else {
            return super.serialize(object);
        }
    }

}
