package com.ballcat.core.validation;

import com.hccake.ballcat.common.core.validation.constraints.OneOfInts;
import com.hccake.ballcat.common.core.validation.constraints.OneOfStrings;
import com.hccake.ballcat.common.core.validation.constraints.ValueOfEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hccake
 */
@Data
@AllArgsConstructor
public class Demo {

	@OneOfStrings({ "张三", "李四" })
	private String name;

	@OneOfInts({ 1, 0 })
	private int statusValue;

	@ValueOfEnum(enumClass = StatusEnum.class, method = "fromValue")
	private int status;

	@ValueOfEnum(enumClass = StatusEnum.class)
	private String statusName;

}
