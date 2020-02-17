package com.hccake.ballcat.codegen.controller;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hccake.ballcat.codegen.config.GenConfig;
import com.hccake.ballcat.codegen.service.GeneratorService;
import com.hccake.ballcat.codegen.vo.GenConfigVO;
import com.hccake.ballcat.codegen.vo.GeneratorVO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 代码生成器
 *
 * @author hccake
 * @date 2018-07-30
 */
@CrossOrigin
@RestController
@RequestMapping("/generator")
@RequiredArgsConstructor
public class GeneratorController {
    private final GeneratorService generatorService;
    private final GenConfig defaultGenConfig;


    /**
     * 默认配置
     *
     * @return 默认配置
     */
    @GetMapping("/config")
    public R<GenConfig> getConfig() {
        return R.ok(defaultGenConfig);
    }


    /**
     * 列表
     *
     * @param tableName 参数集
     * @return 数据库表
     */
    @GetMapping("/page")
    public R getPage(Page page, String tableName) {
        return R.ok(generatorService.getPage(page, tableName).getRecords());
    }

    /**
     * 生成代码
     */
    @SneakyThrows
    @PostMapping("/code")
    public void generatorCode(@RequestBody GeneratorVO generatorVo, HttpServletResponse response) {

		GenConfigVO vo =  generatorVo.getGenConfigVO();
		GenConfig genConfig = vo.transform(defaultGenConfig);

        String[] tableNames = generatorVo.getTableNames();
        byte[] data = generatorService.generatorCode(tableNames, genConfig);
        response.reset();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ballcat.zip\"");
        response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(data.length));
        response.setContentType("application/octet-stream; charset=UTF-8");

        IoUtil.write(response.getOutputStream(), Boolean.TRUE, data);
    }
}
