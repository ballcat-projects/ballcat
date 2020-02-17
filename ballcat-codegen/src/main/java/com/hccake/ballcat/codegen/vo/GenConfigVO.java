package com.hccake.ballcat.codegen.vo;

import cn.hutool.core.bean.BeanUtil;
import com.hccake.ballcat.codegen.config.GenConfig;
import lombok.Data;

/**
 * @author Hccake
 * 生成配置
 */
@Data
public class GenConfigVO {

    /**
     * 包名
     */
    private String packageName;
    /**
     * 作者
     */
    private String author;
    /**
     * 模块名称
     */
    private String moduleName;
    /**
     * 表前缀
     */
    private String tablePrefix;


    public GenConfig transform(GenConfig defaultGenConfig) {
        GenConfig genConfig = new GenConfig();
        BeanUtil.copyProperties(defaultGenConfig, genConfig);

        if(packageName != null){
            genConfig.setPackageName(packageName);
        }
        if(author != null){
            genConfig.setAuthor(author);
        }
        if(moduleName != null){
            genConfig.setModuleName(moduleName);
        }
        if(tablePrefix != null){
            genConfig.setTablePrefix(tablePrefix);
        }
        return genConfig;
    }

}
