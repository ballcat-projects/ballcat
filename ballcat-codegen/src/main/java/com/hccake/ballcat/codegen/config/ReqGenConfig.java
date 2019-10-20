package com.hccake.ballcat.codegen.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/12 15:48
 */
public class ReqGenConfig extends AbstractGenConfig {


    @Override
    public GenConfig mergeConfig(GenConfig sourceConfig) {

        if(StrUtil.isBlank(this.getAuthor())){
            this.setAuthor(sourceConfig.getAuthor());
        }
        if(StrUtil.isBlank(this.getModuleName())){
            this.setModuleName(sourceConfig.getModuleName());
        }
        if(StrUtil.isBlank(this.getPackageName())){
            this.setPackageName(sourceConfig.getPackageName());
        }
        if(StrUtil.isBlank(this.getTablePrefix())){
            this.setTablePrefix(sourceConfig.getTablePrefix());
        }
        if(CollUtil.isEmpty(this.getTypeMapping())){
            this.setTypeMapping(sourceConfig.getTypeMapping());
        }
        return this;
    }


}
