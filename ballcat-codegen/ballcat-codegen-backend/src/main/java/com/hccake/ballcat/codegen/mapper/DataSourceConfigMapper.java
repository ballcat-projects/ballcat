package com.hccake.ballcat.codegen.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hccake.ballcat.codegen.model.entity.DataSourceConfig;
import com.hccake.ballcat.codegen.model.vo.DataSourceConfigVO;
import com.hccake.ballcat.common.core.vo.SelectData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据源
 *
 * @author hccake
 * @date 2020-06-17 10:24:47
 */
public interface DataSourceConfigMapper extends BaseMapper<DataSourceConfig> {

	/**
	 * 分页查询
	 * @param page
	 * @param wrapper
	 * @return VO分页数据
	 */
	IPage<DataSourceConfigVO> selectPageVo(IPage<?> page, @Param(Constants.WRAPPER) Wrapper<DataSourceConfig> wrapper);

	/**
	 * 获取SelectData集合
	 * @return List<SelectData<?>>
	 */
	List<SelectData<?>> listSelectData();

}
