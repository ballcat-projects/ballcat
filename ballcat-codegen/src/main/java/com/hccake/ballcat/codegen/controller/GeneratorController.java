package com.hccake.ballcat.codegen.controller;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hccake.ballcat.codegen.config.DefaultGenConfig;
import com.hccake.ballcat.codegen.config.GenConfig;
import com.hccake.ballcat.codegen.config.ReqGenConfig;
import com.hccake.ballcat.codegen.service.GeneratorService;
import com.hccake.ballcat.codegen.vo.GeneratorVo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

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
	private final DefaultGenConfig defaultGenConfig;

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
	public void generatorCode(@RequestBody GeneratorVo generatorVo, HttpServletResponse response) {

		GenConfig genConfig = Optional.ofNullable(generatorVo.getGenConfig()).orElse(new ReqGenConfig());
		genConfig = genConfig.mergeConfig(defaultGenConfig);

		String[] tableNames = generatorVo.getTableNames();
		byte[] data = generatorService.generatorCode(tableNames, genConfig);
		response.reset();
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ballcat.zip\"");
		response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(data.length));
		response.setContentType("application/octet-stream; charset=UTF-8");

		IoUtil.write(response.getOutputStream(), Boolean.TRUE, data);
	}
}
