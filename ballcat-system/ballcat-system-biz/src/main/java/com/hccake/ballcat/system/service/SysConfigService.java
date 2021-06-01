package com.hccake.ballcat.system.service;

import com.hccake.ballcat.system.model.entity.SysConfig;
import com.hccake.ballcat.system.model.qo.SysConfigQO;
import com.hccake.ballcat.system.model.vo.SysConfigPageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;

/**
 * 系统配置表
 *
 * @author ballcat code generator
 * @date 2019-10-14 17:42:23
 */
public interface SysConfigService extends ExtendService<SysConfig> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param sysConfigQO 查询参数对象
	 * @return 分页数据
	 */
	PageResult<SysConfigPageVO> queryPage(PageParam pageParam, SysConfigQO sysConfigQO);

	/**
	 * 根据配置key获取对应value
	 * @param confKey 配置key
	 * @return confValue
	 */
	String getConfValueByKey(String confKey);

	/**
	 * 根据 confKey 进行更新
	 * @param sysConfig 系统配置
	 * @return 更新是否成功
	 */
	boolean updateByKey(SysConfig sysConfig);

	/**
	 * 根据 confKey 进行删除
	 * @param confKey 配置key
	 * @return 删除是否成功
	 */
	boolean removeByKey(String confKey);

}
