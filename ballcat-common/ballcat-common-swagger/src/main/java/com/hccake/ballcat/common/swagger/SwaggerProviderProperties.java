package com.hccake.ballcat.common.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/11/1 20:05
 */
@Data
@Component
@ConfigurationProperties("swagger.provider")
public class SwaggerProviderProperties {

    /**
     * 聚合文档源信息
     */
    private List<SwaggerResource> resources = new ArrayList<>();
}
