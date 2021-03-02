package com.hccake.ballcat.codegen.controller;

import com.hccake.ballcat.codegen.model.converter.TemplateModelConverter;
import com.hccake.ballcat.codegen.model.dto.TemplateDirectoryCreateDTO;
import com.hccake.ballcat.codegen.model.entity.TemplateDirectoryEntry;
import com.hccake.ballcat.codegen.model.vo.TemplateDirectoryEntryVO;
import com.hccake.ballcat.codegen.service.TemplateDirectoryEntryService;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.model.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
	 * @param templateGroupId 模板组ID
	 * @return R
	 */
	@ApiOperation(value = "指定模板组的文件目录项", notes = "指定模板组的文件目录项")
	@GetMapping("/list/{templateGroupId}")
	// @PreAuthorize("@per.hasPermission('codegen:templatedirectoryentry:read')" )
	public R<List<TemplateDirectoryEntryVO>> getTemplateDirectoryEntryPage(@PathVariable Integer templateGroupId) {
		List<TemplateDirectoryEntry> entries = templateDirectoryEntryService.listByTemplateGroupId(templateGroupId);
		List<TemplateDirectoryEntryVO> vos = entries.stream().map(TemplateModelConverter.INSTANCE::entryPoToVo)
				.collect(Collectors.toList());
		return R.ok(vos);
	}

	/**
	 * 移动目录项
	 * @param entryId 被移动的目录项ID
	 * @param horizontalMove 是否移动到目标目录平级，否则移动到其内部
	 * @param targetEntryId 目标目录项ID
	 * @return R
	 */
	@ApiOperation(value = "移动目录项", notes = "移动目录项")
	@PatchMapping("/{entryId}/position")
	public R<?> move(@PathVariable Integer entryId, @RequestParam boolean horizontalMove,
			@RequestParam Integer targetEntryId) {
		return templateDirectoryEntryService.move(horizontalMove, entryId, targetEntryId) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "移动目录项失败");
	}

	/**
	 * 重命名目录项
	 * @param entryId 目录项ID
	 * @param name 名称
	 * @return R
	 */
	@ApiOperation(value = "重命名目录项", notes = "重命名目录项")
	@PatchMapping("/{entryId}/name")
	public R<?> rename(@PathVariable Integer entryId, @RequestParam String name) {
		return templateDirectoryEntryService.rename(entryId, name) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "重命名目录项");
	}

	/**
	 * 新增模板文件目录项
	 * @param templateDirectoryCreateDTO 模板文件目录项
	 * @return R
	 */
	@ApiOperation(value = "新增模板文件目录项", notes = "新增模板文件目录项")
	// @CreateOperationLogging(msg = "新增模板文件目录项" )
	@PostMapping
	// @PreAuthorize("@per.hasPermission('codegen:templatedirectoryentry:add')" )
	public R<?> save(@RequestBody TemplateDirectoryCreateDTO templateDirectoryCreateDTO) {

		return templateDirectoryEntryService.createEntry(templateDirectoryCreateDTO) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增模板文件目录项失败");
	}

	/**
	 * 通过id删除模板文件目录项
	 * @param id id
	 * @param mode 删除模式， 1：只删除本身，将子节点上移 2. 删除自身及其所有子节点
	 * @return R
	 */
	@ApiOperation(value = "通过id删除模板文件目录项", notes = "通过id删除模板文件目录项")
	// @DeleteOperationLogging(msg = "通过id删除模板文件目录项" )
	@DeleteMapping("/{id}")
	// @PreAuthorize("@per.hasPermission('codegen:templatedirectoryentry:del')" )
	public R<?> removeById(@PathVariable Integer id, @RequestParam Integer mode) {
		return templateDirectoryEntryService.removeEntry(id, mode) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除模板文件目录项失败");
	}

}