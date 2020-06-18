package com.hccake.ballcat.codegen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hccake.ballcat.codegen.model.dto.DataSourceConfigDTO;
import com.hccake.ballcat.codegen.model.entity.DataSourceConfig;
import com.hccake.ballcat.codegen.model.qo.DataSourceConfigQO;
import com.hccake.ballcat.codegen.model.vo.DataSourceConfigVO;
import com.hccake.ballcat.common.core.vo.SelectData;

import java.util.List;


/**
 * 数据源
 *
 * @author hccake
 * @date 2020-06-17 10:24:47
 */
public interface DataSourceConfigService extends IService<DataSourceConfig> {
    /**
    *  根据QueryObeject查询分页数据
    * @param page 分页参数
    * @param qo 查询参数对象
    * @return  分页数据
    */
    IPage<DataSourceConfigVO> selectPageVo(IPage<?> page, DataSourceConfigQO qo);

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
