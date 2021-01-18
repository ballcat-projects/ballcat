package com.hccake.ballcat.codegen.service;

import com.hccake.ballcat.codegen.model.bo.TemplateFile;
import com.hccake.ballcat.codegen.model.entity.TemplateGroup;
import com.hccake.ballcat.codegen.model.qo.TemplateGroupQO;
import com.hccake.ballcat.codegen.model.vo.TemplateGroupVO;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.domain.PageResult;
import com.hccake.ballcat.common.core.domain.SelectData;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.List;
import java.util.Set;

/**
 * 模板组
 *
 * @author hccake
 * @date 2020-06-19 19:11:41
 */
public interface TemplateGroupService extends ExtendService<TemplateGroup> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return 分页数据
	 */
	PageResult<TemplateGroupVO> queryPage(PageParam pageParam, TemplateGroupQO qo);

	/**
	 * 查找指定模板组下所有的模板文件
	 * @param groupId 模板组ID
	 * @param templateFileIds 模板文件ID集合
	 * @return List<TemplateFile>
	 */
	List<TemplateFile> findTemplateFiles(Integer groupId, Set<Integer> templateFileIds);

	/**
	 * 获取SelectData数据
	 * @return List<SelectData<?>>
	 */
	List<SelectData<?>> getSelectData();

	/**
	 * 复制模板组
	 * @param resourceId 原模板组ID
	 * @param templateGroup 目标模板组
	 * @return boolean 复制成功: true
	 */
	boolean copy(Integer resourceId, TemplateGroup templateGroup);

}