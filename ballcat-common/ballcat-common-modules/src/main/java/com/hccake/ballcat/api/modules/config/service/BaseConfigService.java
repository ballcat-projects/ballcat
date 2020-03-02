package com.hccake.ballcat.api.modules.config.service;

import com.hccake.ballcat.api.modules.config.model.entity.BaseConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 系统配置表
 *
 * @author ballcat code generator
 * @date 2019-10-14 17:42:23
 */
public interface BaseConfigService extends IService<BaseConfig> {

    /**
     * 根据配置key获取对应value
     * @param confKey
     * @return confValue
     */
    String getConfValueByKey(String confKey);
}
