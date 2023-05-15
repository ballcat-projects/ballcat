package com.hccake.ballcat.common.security.jackson2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * This mixin class is used to serialize/deserialize {@link Long}.
 *
 * @author Hccake
 * @since 1.3.0
 * @see Long
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public abstract class LongMixin {

	@JsonCreator
	LongMixin(Long value) {
	}

}
