package com.hccake.ballcat.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysConfig;
import com.hccake.ballcat.admin.modules.sys.service.SysConfigService;
import com.hccake.ballcat.admin.modules.sys.mapper.SysConfigMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 系统配置表
 *
 * @author ballcat code generator
 * @date 2019-10-14 17:42:23
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    /**
     * 根据配置key获取对应value
     *
     * @param confKey 缓存对应key
     * @return confValue
     */
    @Override
    public String getConfValueByKey(String confKey) {
        SysConfig sysConfig = baseMapper.selectOne(Wrappers.<SysConfig>lambdaQuery().eq(SysConfig::getConfKey, confKey));
        return sysConfig == null ? "": sysConfig.getConfValue();
    }
}
