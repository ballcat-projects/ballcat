package com.hccake.ballcat.codegen.service;

import com.hccake.ballcat.codegen.model.dto.GeneratorOptionDTO;

/**
 * @author hccake
 * @date 2018/7/29
 */
public interface GeneratorService {
	/**
	 * 生成代码
	 * @param generatorOptionDTO 代码生成的一些参数
	 * @return 已生成的代码数据
	 */
	byte[] generatorCode(GeneratorOptionDTO generatorOptionDTO);

}
