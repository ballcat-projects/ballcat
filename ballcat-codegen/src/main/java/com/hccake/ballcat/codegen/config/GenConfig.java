package com.hccake.ballcat.codegen.config;

import java.util.Map;
import java.util.Set;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/12 20:11
 */
public interface GenConfig {

    /**
     * 基础包名
     * @return
     */
    String getPackageName();

    /**
     * 作者
     * @return
     */
    String getAuthor();

    /**
     * 模块名
     * @return
     */
    String getModuleName();

    /**
     * 表前缀
     * @return
     */
    String getTablePrefix();


    /**
     * 类型映射
     * @return
     */
    Map<String, String> getTypeMapping();

    /**
     * swagger文档中隐藏的属性字段
     * @return
     */
    Set<String> getHiddenColumns();


    /**
     * 合并配置项
     * @param sourceConfig
     * @return
     */
    GenConfig mergeConfig(GenConfig sourceConfig);

}