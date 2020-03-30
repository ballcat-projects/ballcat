package com.hccake.ballcat.common.modules.config.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.common.modules.config.mapper.BaseConfigMapper;
import com.hccake.ballcat.common.modules.config.model.entity.BaseConfig;
import com.hccake.ballcat.common.modules.config.service.BaseConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 系统配置表
 *
 * @author ballcat code generator
 * @date 2019-10-14 17:42:23
 */
@Service
public class BaseConfigServiceImpl extends ServiceImpl<BaseConfigMapper, BaseConfig> implements BaseConfigService {

    /**
     * 根据配置key获取对应value
     *
     * @param confKey 缓存对应key
     * @return confValue
     */
    @Override
    public String getConfValueByKey(String confKey) {
        BaseConfig baseConfig = baseMapper.selectOne(Wrappers.<BaseConfig>lambdaQuery().eq(BaseConfig::getConfKey, confKey));
        return baseConfig == null ? "": baseConfig.getConfValue();
    }
}
