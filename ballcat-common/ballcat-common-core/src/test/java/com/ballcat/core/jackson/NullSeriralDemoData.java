package com.ballcat.core.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Collection;
import java.util.Map;

/**
 * jackson 测试序列化的对象
 *
 * @author hccake
 */
@Data
public class NullSeriralDemoData {

	private String str;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String strIgnore;

	private Map<String, String> map;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Map<String, String> mapIgnore;

	private Collection<String> collection;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Collection<String> collectionIgnore;

}
