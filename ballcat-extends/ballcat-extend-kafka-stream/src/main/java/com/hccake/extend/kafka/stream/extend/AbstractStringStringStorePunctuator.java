package com.hccake.extend.kafka.stream.extend;

import lombok.extern.slf4j.Slf4j;

/**
 * String String 类型的 store
 *
 * @author lingting 2020/6/19 10:21
 */
@Slf4j
public abstract class AbstractStringStringStorePunctuator<R>
		extends AbstractKeyValueStorePunctuator<String, String, R> {

}
