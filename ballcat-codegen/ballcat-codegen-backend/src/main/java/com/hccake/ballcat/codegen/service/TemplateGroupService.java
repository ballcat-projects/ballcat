package com.hccake.ballcat.codegen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hccake.ballcat.codegen.model.bo.TemplateFile;
import com.hccake.ballcat.codegen.model.entity.TemplateGroup;
import com.hccake.ballcat.codegen.model.vo.TemplateGroupVO;
import com.hccake.ballcat.codegen.model.qo.TemplateGroupQO;
import com.hccake.ballcat.common.core.vo.SelectData;

import java.util.List;

/**
 * 模板组
 *
 * @author hccake
 * @date 2020-06-19 19:11:41
 */
public interface TemplateGroupService extends IService<TemplateGroup> {

	/**
	 * 根据QueryObeject查询分页数据
	 * @param page 分页参数
	 * @param qo 查询参数对象
	 * @return 分页数据
	 */
	IPage<TemplateGroupVO> selectPageVo(IPage<?> page, TemplateGroupQO qo);

	/**
	 * 查找指定模板组下所有的模板文件
	 * @param groupId 模板组ID
	 * @return List<TemplateFile>
	 */
	List<TemplateFile> findTemplateFiles(Integer groupId);

	/**
	 * 获取SelectData数据
	 * @return List<SelectData<?>>
	 */
    List<SelectData<?>> getSelectData();
}