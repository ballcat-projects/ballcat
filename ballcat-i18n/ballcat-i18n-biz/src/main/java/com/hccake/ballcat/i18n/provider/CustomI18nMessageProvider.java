package com.hccake.ballcat.i18n.provider;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.hccake.ballcat.common.i18n.I18nMessage;
import com.hccake.ballcat.common.i18n.I18nMessageProvider;
import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.ballcat.i18n.constant.I18nRedisKeyConstants;
import com.hccake.ballcat.i18n.model.dto.I18nDataUnique;
import com.hccake.ballcat.i18n.model.entity.I18nData;
import com.hccake.ballcat.i18n.service.I18nDataService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 自定义的 I18nMessageProvider，数据存储在数据库中，使用内存缓存提高查询效率
 *
 * @author hccake
 */
@Component
public class CustomI18nMessageProvider implements I18nMessageProvider, MessageListener {

	private final I18nDataService i18nDataService;

	private final StringRedisTemplate stringRedisTemplate;

	private final TimedCache<String, I18nMessage> cache;

	public CustomI18nMessageProvider(I18nDataService i18nDataService, StringRedisTemplate stringRedisTemplate) {
		this.i18nDataService = i18nDataService;
		this.stringRedisTemplate = stringRedisTemplate;
		// 默认过期时间设置为 1 小时
		int millisecondsOfHour = 1000 * 60 * 60;
		this.cache = CacheUtil.newTimedCache(millisecondsOfHour);
		// 每秒检查一次过期
		this.cache.schedulePrune(1000);
	}

	@Override
	public I18nMessage getI18nMessage(String code, Locale locale) {
		String languageTag = locale.toLanguageTag();

		// 缓存 key
		String cacheKey = getCacheKey(code, languageTag);
		I18nMessage i18nMessage = cache.get(cacheKey);

		// 如果缓存没有，则查询数据库（这里数据库查询其实也加了 redis 缓存）
		if (i18nMessage == null) {
			I18nData i18nData = i18nDataService.getByCodeAndLanguageTag(code, languageTag);
			if (i18nData != null) {
				i18nMessage = converterToI18nMessage(i18nData);
				cache.put(cacheKey, i18nMessage);
			}
		}

		return i18nMessage;
	}

	/**
	 * 缓存 key
	 * @param code i18n 数据的唯一标识
	 * @param languageTag 语言标签
	 * @return String cacheKey
	 */
	private String getCacheKey(String code, String languageTag) {
		return code + ":" + languageTag;
	}

	/**
	 * 转换 i18nData to I18nMessage
	 * @param i18nData 数据库存储对象
	 * @return I18nMessage
	 */
	private I18nMessage converterToI18nMessage(I18nData i18nData) {
		I18nMessage i18nMessage = new I18nMessage();
		i18nMessage.setMessage(i18nData.getMessage());
		i18nMessage.setCode(i18nData.getCode());
		i18nMessage.setLanguageTag(i18nData.getCode());
		return i18nMessage;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		byte[] channelBytes = message.getChannel();
		RedisSerializer<String> stringSerializer = stringRedisTemplate.getStringSerializer();
		String channel = stringSerializer.deserialize(channelBytes);

		// 这里没有使用通配符，所以一定是true
		if (I18nRedisKeyConstants.CHANNEL_I18N_DATA_UPDATED.equals(channel)) {
			byte[] bodyBytes = message.getBody();
			String body = stringSerializer.deserialize(bodyBytes);
			I18nDataUnique i18nDataUnique = JsonUtils.toObj(body, I18nDataUnique.class);
			String cacheKey = getCacheKey(i18nDataUnique.getCode(), i18nDataUnique.getLanguageTag());
			cache.remove(cacheKey);
		}
	}

}
