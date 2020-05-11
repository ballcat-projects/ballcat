package com.hccake.ballcat.common.swagger.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/11/1 20:05
 */
@Data
@ConfigurationProperties("swagger.aggregator")
public class SwaggerAggregatorProperties {

    /**
     * 聚合文档源信息
     */
    private List<SwaggerResource> providerResources = new ArrayList<>();
}
