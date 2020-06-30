package com.hccake.ballcat.codegen.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hccake.ballcat.codegen.model.entity.TemplateGroup;
import com.hccake.ballcat.codegen.model.qo.TemplateGroupQO;
import com.hccake.ballcat.codegen.model.vo.TemplateGroupVO;
import com.hccake.ballcat.codegen.service.TemplateGroupService;
import com.hccake.ballcat.common.core.result.BaseResultCode;
import com.hccake.ballcat.common.core.result.R;
import com.hccake.ballcat.common.core.vo.SelectData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模板组
 *
 * @author hccake
 * @date 2020-06-19 19:11:41
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/gen/template/group")
@Api(value = "/template/group", tags = "模板组管理")
public class TemplateGroupController {

	private final TemplateGroupService templateGroupService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param templateGroupQO 模板组
	 * @return R
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	// @PreAuthorize("@per.hasPermission('codegen:templategroup:read')" )
	public R<IPage<TemplateGroupVO>> getTemplateGroupPage(Page<?> page, TemplateGroupQO templateGroupQO) {
		return R.ok(templateGroupService.selectPageVo(page, templateGroupQO));
	}

	/**
	 * 通过id查询模板组
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	// @PreAuthorize("@per.hasPermission('codegen:templategroup:read')" )
	public R<TemplateGroup> getById(@PathVariable("id") Integer id) {
		return R.ok(templateGroupService.getById(id));
	}

	/**
	 * 新增模板组
	 * @param templateGroup 模板组
	 * @return R
	 */
	@ApiOperation(value = "新增模板组", notes = "新增模板组")
	// @CreateOperationLogging(msg = "新增模板组" )
	@PostMapping
	// @PreAuthorize("@per.hasPermission('codegen:templategroup:add')" )
	public R<?> save(@RequestBody TemplateGroup templateGroup) {
		return templateGroupService.save(templateGroup) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增模板组失败");
	}


	/**
	 * 复制模板组
	 * @param resourceId 原模板组id
	 * @param templateGroup 新模板组实体
	 * @return R
	 */
	@PostMapping("/{resourceId}")
	@ApiOperation(value = "复制模板组", notes = "复制模板组")
	public R<?> copy(@PathVariable Integer resourceId,
			@RequestBody TemplateGroup templateGroup) {
		return templateGroupService.copy(resourceId, templateGroup) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "复制模板组失败");
	}



	/**
	 * 修改模板组
	 * @param templateGroup 模板组
	 * @return R
	 */
	@ApiOperation(value = "修改模板组", notes = "修改模板组")
	// @UpdateOperationLogging(msg = "修改模板组" )
	@PutMapping
	// @PreAuthorize("@per.hasPermission('codegen:templategroup:edit')" )
	public R updateById(@RequestBody TemplateGroup templateGroup) {
		return templateGroupService.updateById(templateGroup) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改模板组失败");
	}

	/**
	 * 通过id删除模板组
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除模板组", notes = "通过id删除模板组")
	// @DeleteOperationLogging(msg = "通过id删除模板组" )
	@DeleteMapping("/{id}")
	// @PreAuthorize("@per.hasPermission('codegen:templategroup:del')" )
	public R removeById(@PathVariable Integer id) {
		return templateGroupService.removeById(id) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除模板组失败");
	}


	/**
	 * 获取模板组选择框数据
	 * @return R
	 */
	@ApiOperation(value = "获取模板组选择框", notes = "获取模板组选择框")
	// @DeleteOperationLogging(msg = "通过id删除模板组" )
	@GetMapping("/select")
	// @PreAuthorize("@per.hasPermission('codegen:templategroup:del')" )
	public R<List<SelectData<?>>> getSelectData() {
		return R.ok(templateGroupService.getSelectData());
	}


}