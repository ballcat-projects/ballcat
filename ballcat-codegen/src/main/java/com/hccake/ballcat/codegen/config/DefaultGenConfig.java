package com.hccake.ballcat.codegen.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/12 15:47
 */
@Component
@ConfigurationProperties(prefix = "gen")
@PropertySource("generator.properties")
public class DefaultGenConfig extends AbstractGenConfig {


    private GenConfig shield() {
        throw new RuntimeException("default genconfig no modification allowed");
    }

    public String setMainPath() {
        shield();
        return null;
    }

    public String setPackageName() {
        shield();
        return null;
    }

    public String setAuthor() {
        shield();
        return null;
    }

    public String setModuleName() {
        shield();
        return null;
    }

    public String setTablePrefix() {
        shield();
        return null;
    }

    public Map<String, String> setTypeMapping() {
        shield();
        return null;
    }

    public Set<String> setHiddenColumns() {
        shield();
        return null;
    }


    @Override
    public GenConfig mergeConfig(GenConfig sourceConfig) {
        return shield();
    }

}
