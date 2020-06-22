package com.hccake.ballcat.codegen.controller;

import com.hccake.ballcat.codegen.model.entity.TemplateDirectoryEntry;
import com.hccake.ballcat.codegen.model.vo.TemplateDirectoryEntryVO;
import com.hccake.ballcat.codegen.service.TemplateDirectoryEntryService;
import com.hccake.ballcat.common.core.result.BaseResultCode;
import com.hccake.ballcat.common.core.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 模板文件目录项
 *
 * @author hccake
 * @date 2020-06-19 19:11:41
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/gen/template/directory-entry")
@Api(value = "/gen/template/directory-entry", tags = "模板文件目录项管理")
public class TemplateDirectoryEntryController {
	private final TemplateDirectoryEntryService templateDirectoryEntryService;

	/**
	 * 模板组的文件目录
	 *
	 * @param templateGroupId 模板组ID
	 * @return R
	 */
	@ApiOperation(value = "指定模板组的文件目录项", notes = "指定模板组的文件目录项")
	@GetMapping("/list/{templateGroupId}")
	//@PreAuthorize("@per.hasPermission('codegen:templatedirectoryentry:read')" )
	public R<List<TemplateDirectoryEntryVO>> getTemplateDirectoryEntryPage(@PathVariable Integer templateGroupId) {
		return R.ok(templateDirectoryEntryService.queryDirectoryEntry(templateGroupId));
	}


	/**
	 * 新增模板文件目录项
	 *
	 * @param templateDirectoryEntry 模板文件目录项
	 * @return R
	 */
	@ApiOperation(value = "新增模板文件目录项", notes = "新增模板文件目录项")
	// @CreateOperationLogging(msg = "新增模板文件目录项" )
	@PostMapping
	//@PreAuthorize("@per.hasPermission('codegen:templatedirectoryentry:add')" )
	public R save(@RequestBody TemplateDirectoryEntry templateDirectoryEntry) {
		return templateDirectoryEntryService.save(templateDirectoryEntry) ?
				R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增模板文件目录项失败");
	}

	/**
	 * 修改模板文件目录项
	 *
	 * @param templateDirectoryEntry 模板文件目录项
	 * @return R
	 */
	@ApiOperation(value = "修改模板文件目录项", notes = "修改模板文件目录项")
	//@UpdateOperationLogging(msg = "修改模板文件目录项" )
	@PutMapping
	//@PreAuthorize("@per.hasPermission('codegen:templatedirectoryentry:edit')" )
	public R updateById(@RequestBody TemplateDirectoryEntry templateDirectoryEntry) {
		return templateDirectoryEntryService.updateById(templateDirectoryEntry) ?
				R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改模板文件目录项失败");
	}

	/**
	 * 通过id删除模板文件目录项
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除模板文件目录项", notes = "通过id删除模板文件目录项")
	//@DeleteOperationLogging(msg = "通过id删除模板文件目录项" )
	@DeleteMapping("/{id}")
	//@PreAuthorize("@per.hasPermission('codegen:templatedirectoryentry:del')" )
	public R removeById(@PathVariable Integer id) {
		return templateDirectoryEntryService.removeById(id) ?
				R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除模板文件目录项失败");
	}


	/**
	 * 移动目录项
	 * @param entryId  被移动的目录项ID
	 * @param horizontalMove 是否移动到目标目录平级，否则移动到其内部
	 * @param targetEntryId 目标目录项ID
	 * @return R
	 */
	@ApiOperation(value = "移动目录项", notes = "移动目录项")
	@PatchMapping("/{entryId}/position")
	public R<?> move(
				  @PathVariable Integer entryId,
				  @RequestParam boolean horizontalMove,
				  @RequestParam Integer targetEntryId
	) {
		return templateDirectoryEntryService.move(horizontalMove, entryId, targetEntryId)?
		 R.ok(): R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "移动目录项失败");
	}


	/**
	 * 重命名目录项
	 * @param entryId  目录项ID
	 * @param name 名称
	 * @return R
	 */
	@ApiOperation(value = "重命名目录项", notes = "重命名目录项")
	@PatchMapping("/{entryId}/name")
	public R<?> rename(@PathVariable Integer entryId, @RequestParam String name) {
		return templateDirectoryEntryService.rename(entryId, name)?
				R.ok(): R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "重命名目录项");
	}

}