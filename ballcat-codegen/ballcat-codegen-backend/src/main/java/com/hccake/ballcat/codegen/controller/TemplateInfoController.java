package com.hccake.ballcat.codegen.controller;

import com.hccake.ballcat.codegen.model.entity.TemplateInfo;
import com.hccake.ballcat.codegen.service.TemplateInfoService;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.model.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模板信息
 *
 * @author hccake
 * @date 2020-06-18 18:32:51
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/gen/template/info")
@Api(value = "/gen/template/info", tags = "模板信息管理")
public class TemplateInfoController {

	private final TemplateInfoService templateInfoService;

	/**
	 * 指定模板组的文件列表
	 * @param templateGroupId 模板组ID
	 * @return R
	 */
	@ApiOperation(value = "指定模板组的文件列表", notes = "指定模板组的文件列表")
	@GetMapping("/list/{templateGroupId}")
	public R<List<TemplateInfo>> listTemplateInfo(@PathVariable Integer templateGroupId) {
		return R.ok(templateInfoService.listByTemplateGroupId(templateGroupId));
	}

	/**
	 * 通过id查询模板信息
	 * @param directoryEntryId directoryEntryId
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{directoryEntryId}")
	// @PreAuthorize("@per.hasPermission('gen:template:read')" )
	public R<TemplateInfo> getById(@PathVariable("directoryEntryId") Integer directoryEntryId) {
		return R.ok(templateInfoService.getById(directoryEntryId));
	}

	/**
	 * 修改模板信息
	 * @param genTemplateInfo 模板信息
	 * @return R
	 */
	@ApiOperation(value = "修改模板信息", notes = "修改模板信息")
	// @UpdateOperationLogging(msg = "修改模板信息" )
	@PutMapping
	// @PreAuthorize("@per.hasPermission('gen:template:edit')" )
	public R updateById(@RequestBody TemplateInfo genTemplateInfo) {
		return templateInfoService.updateById(genTemplateInfo) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改模板信息失败");
	}

}
