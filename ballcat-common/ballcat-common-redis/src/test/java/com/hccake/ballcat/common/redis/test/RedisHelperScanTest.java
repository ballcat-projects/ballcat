package com.hccake.ballcat.common.redis.test;

import com.hccake.ballcat.common.redis.RedisHelper;
import com.hccake.ballcat.common.redis.prefix.impl.DefaultRedisPrefixConverter;
import com.hccake.ballcat.common.redis.serialize.PrefixStringRedisSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.KeyScanOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RedisHelperScanTest {

	private RedisTemplate<String, String> originalRedisTemplate;

	private RedisTemplate<String, String> redisTemplate;

	private Cursor<String> cursor;

	@BeforeEach
	@SuppressWarnings("unchecked")
	void setUp() {
		originalRedisTemplate = RedisHelper.getRedisTemplate();
		redisTemplate = mock(RedisTemplate.class);
		cursor = mock(Cursor.class);
		doReturn(new PrefixStringRedisSerializer(new DefaultRedisPrefixConverter("global:"))).when(redisTemplate)
			.getKeySerializer();
		when(redisTemplate.scan(any(ScanOptions.class))).thenReturn(cursor);
		RedisHelper.setRedisTemplate(redisTemplate);
	}

	@AfterEach
	void tearDown() {
		RedisHelper.setRedisTemplate(originalRedisTemplate);
	}

	@Test
	void scanShouldSerializeLogicalPatternWithConfiguredKeySerializer() {
		Cursor<String> result = RedisHelper.scan("cache:*");

		ScanOptions scanOptions = captureScanOptions();
		assertSame(cursor, result);
		assertArrayEquals("global:cache:*".getBytes(StandardCharsets.UTF_8), scanOptions.getBytePattern());
	}

	@Test
	void scanShouldPreserveCountWhenSerializingLogicalPattern() {
		RedisHelper.scan("cache:*", 100);

		ScanOptions scanOptions = captureScanOptions();
		assertArrayEquals("global:cache:*".getBytes(StandardCharsets.UTF_8), scanOptions.getBytePattern());
		assertEquals(100L, scanOptions.getCount());
	}

	@Test
	void scanOptionsShouldSerializeLogicalPatternAndPreserveOptions() {
		ScanOptions logicalOptions = ScanOptions.scanOptions().match("cache:*").count(50).type(DataType.STRING).build();

		Cursor<String> result = RedisHelper.scan(logicalOptions);

		ScanOptions scanOptions = captureScanOptions();
		assertSame(cursor, result);
		assertArrayEquals("global:cache:*".getBytes(StandardCharsets.UTF_8), scanOptions.getBytePattern());
		assertEquals(50L, scanOptions.getCount());
		assertEquals(DataType.STRING.code(), ((KeyScanOptions) scanOptions).getType());
	}

	@Test
	void scanOptionsWithoutPatternShouldStayWithinLogicalNamespace() {
		RedisHelper.scan(ScanOptions.scanOptions().count(25).build());

		ScanOptions scanOptions = captureScanOptions();
		assertArrayEquals("global:*".getBytes(StandardCharsets.UTF_8), scanOptions.getBytePattern());
		assertEquals(25L, scanOptions.getCount());
	}

	@Test
	void scanRawShouldPassPhysicalOptionsThrough() {
		ScanOptions physicalOptions = ScanOptions.scanOptions()
			.match("global:cache:*".getBytes(StandardCharsets.UTF_8))
			.count(50)
			.build();

		Cursor<String> result = RedisHelper.scanRaw(physicalOptions);

		assertSame(cursor, result);
		verify(redisTemplate).scan(physicalOptions);
		verify(redisTemplate, never()).getKeySerializer();
	}

	private ScanOptions captureScanOptions() {
		ArgumentCaptor<ScanOptions> captor = ArgumentCaptor.forClass(ScanOptions.class);
		verify(redisTemplate).scan(captor.capture());
		return captor.getValue();
	}

}
