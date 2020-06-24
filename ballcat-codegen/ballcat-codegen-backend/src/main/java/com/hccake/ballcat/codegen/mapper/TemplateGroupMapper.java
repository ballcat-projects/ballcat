package com.hccake.ballcat.codegen.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hccake.ballcat.codegen.model.entity.TemplateGroup;
import com.hccake.ballcat.codegen.model.vo.TemplateGroupVO;
import org.apache.ibatis.annotations.Param;

/**
 * 模板组
 *
 * @author hccake
 * @date 2020-06-19 19:11:41
 */
public interface TemplateGroupMapper extends BaseMapper<TemplateGroup> {

	/**
	 * 分页查询
	 * @param page
	 * @param wrapper
	 * @return VO分页数据
	 */
	IPage<TemplateGroupVO> selectPageVo(IPage<?> page, @Param(Constants.WRAPPER) Wrapper<TemplateGroup> wrapper);

}