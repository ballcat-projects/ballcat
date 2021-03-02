package com.hccake.ballcat.codegen.controller;

import cn.hutool.core.io.IoUtil;
import com.hccake.ballcat.codegen.model.dto.GeneratorOptionDTO;
import com.hccake.ballcat.codegen.model.qo.TableInfoQO;
import com.hccake.ballcat.codegen.model.vo.TableInfo;
import com.hccake.ballcat.codegen.service.GeneratorService;
import com.hccake.ballcat.codegen.service.TableInfoService;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.R;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping
@RequiredArgsConstructor
public class GenerateController {

	private final GeneratorService generatorService;

	private final TableInfoService tableInfoService;

	/**
	 * 表信息分页查询
	 * @param pageParam 分页参数
	 * @param tableInfoQO 表信息查询对象
	 * @return R
	 */
	@ApiOperation(value = "表信息分页查询", notes = "表信息分页查询")
	@GetMapping("/table-info/page")
	public R<PageResult<TableInfo>> getDataSourceConfigPage(PageParam pageParam, TableInfoQO tableInfoQO) {
		return R.ok(tableInfoService.queryPage(pageParam, tableInfoQO));
	}

	/**
	 * 生成代码
	 */
	@SneakyThrows
	@PostMapping("/generate")
	public void generateCode(@RequestBody GeneratorOptionDTO generatorOptionDTO, HttpServletResponse response) {
		byte[] data = generatorService.generatorCode(generatorOptionDTO);
		response.reset();
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ballcat.zip\"");
		response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(data.length));
		response.setContentType("application/octet-stream; charset=UTF-8");

		IoUtil.write(response.getOutputStream(), Boolean.TRUE, data);
	}

}
