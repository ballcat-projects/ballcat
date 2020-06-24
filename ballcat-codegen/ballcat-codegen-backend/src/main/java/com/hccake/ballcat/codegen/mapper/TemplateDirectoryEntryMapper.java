package com.hccake.ballcat.codegen.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hccake.ballcat.codegen.model.entity.TemplateDirectoryEntry;
import com.hccake.ballcat.codegen.model.vo.TemplateDirectoryEntryVO;
import org.apache.ibatis.annotations.Param;

/**
 * 模板文件目录项
 *
 * @author hccake
 * @date 2020-06-19 19:11:41
 */
public interface TemplateDirectoryEntryMapper extends BaseMapper<TemplateDirectoryEntry> {

	/**
	 * 分页查询
	 * @param page
	 * @param wrapper
	 * @return VO分页数据
	 */
	IPage<TemplateDirectoryEntryVO> selectPageVo(IPage<?> page,
			@Param(Constants.WRAPPER) Wrapper<TemplateDirectoryEntry> wrapper);

}