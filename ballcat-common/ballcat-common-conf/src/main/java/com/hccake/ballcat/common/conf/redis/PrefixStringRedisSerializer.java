package com.hccake.ballcat.common.conf.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/3/27 22:57
 * 自定义Key序列化工具，添加全局key前缀
 */
@Slf4j
public class PrefixStringRedisSerializer extends StringRedisSerializer  {
    private final String prefix;
    private final boolean enable;

    public PrefixStringRedisSerializer(String prefix) {
        super(StandardCharsets.UTF_8);
        this.prefix = prefix;
        this.enable = prefix != null && !"".equals(prefix);
    }

    @Override
    public String deserialize(byte[] bytes) {
        String originKey = super.deserialize(bytes);
        // 如果有全局前缀，则需要删除
        if (enable && originKey != null && originKey.startsWith(prefix)) {
            originKey = originKey.substring(prefix.length());
        }
        return originKey;
    }

    @Override
    public byte[] serialize(String string) {
        if(enable && string != null){
            string = prefix + string;
        }
        return super.serialize(string);
    }
}
