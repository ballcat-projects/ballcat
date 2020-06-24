package com.hccake.ballcat.admin.modules.sys.service;

import com.hccake.ballcat.admin.modules.sys.model.entity.SysConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 系统配置表
 *
 * @author ballcat code generator
 * @date 2019-10-14 17:42:23
 */
public interface SysConfigService extends IService<SysConfig> {

	/**
	 * 根据配置key获取对应value
	 * @param confKey
	 * @return confValue
	 */
	String getConfValueByKey(String confKey);

}
