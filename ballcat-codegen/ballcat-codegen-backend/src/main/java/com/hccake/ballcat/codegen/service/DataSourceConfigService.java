package com.hccake.ballcat.codegen.service;

import com.hccake.ballcat.codegen.model.dto.DataSourceConfigDTO;
import com.hccake.ballcat.codegen.model.entity.DataSourceConfig;
import com.hccake.ballcat.codegen.model.qo.DataSourceConfigQO;
import com.hccake.ballcat.codegen.model.vo.DataSourceConfigVO;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.domain.PageResult;
import com.hccake.ballcat.common.core.domain.SelectData;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.List;

/**
 * 数据源
 *
 * @author hccake
 * @date 2020-06-17 10:24:47
 */
public interface DataSourceConfigService extends ExtendService<DataSourceConfig> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return 分页数据
	 */
	PageResult<DataSourceConfigVO> queryPage(PageParam pageParam, DataSourceConfigQO qo);

	/**
	 * 获取 SelectData 集合
	 * @return List<SelectData<?>> SelectData 集合
	 */
	List<SelectData<?>> listSelectData();

	/**
	 * 保存数据源配置
	 * @param dataSourceConfigDTO 数据源配置信息
	 * @return boolean
	 */
	boolean save(DataSourceConfigDTO dataSourceConfigDTO);

	/**
	 * 更新数据源配置
	 * @param dataSourceConfigDTO 数据源配置信息
	 * @return boolean
	 */
	boolean update(DataSourceConfigDTO dataSourceConfigDTO);

}
