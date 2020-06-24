package com.hccake.ballcat.codegen.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hccake.ballcat.codegen.model.entity.TemplateProperty;
import com.hccake.ballcat.codegen.model.vo.TemplatePropertyVO;
import org.apache.ibatis.annotations.Param;

/**
 * 模板属性配置
 *
 * @author hccake
 * @date 2020-06-22 15:46:39
 */
public interface TemplatePropertyMapper extends BaseMapper<TemplateProperty> {

	/**
	 * 分页查询
	 * @param page
	 * @param wrapper
	 * @return VO分页数据
	 */
	IPage<TemplatePropertyVO> selectPageVo(IPage<?> page, @Param(Constants.WRAPPER) Wrapper<TemplateProperty> wrapper);

}