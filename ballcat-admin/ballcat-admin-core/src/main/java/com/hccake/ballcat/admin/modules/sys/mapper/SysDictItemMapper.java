package com.hccake.ballcat.admin.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysDictItem;
import com.hccake.ballcat.common.core.vo.SelectData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典项
 *
 * @author hccake
 * @date 2020-03-26 18:40:20
 */
public interface SysDictItemMapper extends BaseMapper<SysDictItem> {

	/**
	 * 根据字典标识查询对应字典选择项
	 * @param dictCode 字典标识
	 * @return 对应字典项的SelectData
	 */
	List<SelectData> querySelectDataByDictCode(@Param("dictCode") String dictCode);

}
