package com.hccake.ballcat.codegen.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hccake.ballcat.codegen.model.entity.TemplateProperty;
import com.hccake.ballcat.codegen.model.qo.TemplatePropertyQO;
import com.hccake.ballcat.codegen.model.vo.TemplatePropertyVO;
import com.hccake.ballcat.codegen.service.TemplatePropertyService;
import com.hccake.ballcat.common.core.result.BaseResultCode;
import com.hccake.ballcat.common.core.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 模板属性配置
 *
 * @author hccake
 * @date 2020-06-22 15:46:39
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/gen/template/property")
@Api(value = "/gen/template/property", tags = "模板属性配置管理")
public class TemplatePropertyController {

	private final TemplatePropertyService templatePropertyService;

	/**
	 * 分页查询
	 *
	 * @param page               分页对象
	 * @param templatePropertyQO 模板属性配置
	 * @return R
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	//@PreAuthorize("@per.hasPermission('codegen:templateproperty:read')" )
	public R<IPage<TemplatePropertyVO>> getTemplatePropertyPage(
			Page<?> page, TemplatePropertyQO templatePropertyQO) {
		return R.ok(templatePropertyService.selectPageVo(page, templatePropertyQO));
	}


	/**
	 * 通过id查询模板属性配置
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping("/{id}")
	//@PreAuthorize("@per.hasPermission('codegen:templateproperty:read')" )
	public R<TemplateProperty> getById(@PathVariable("id") Integer id) {
		return R.ok(templatePropertyService.getById(id));
	}

	/**
	 * 新增模板属性配置
	 *
	 * @param templateProperty 模板属性配置
	 * @return R
	 */
	@ApiOperation(value = "新增模板属性配置", notes = "新增模板属性配置")
	//@CreateOperationLogging(msg = "新增模板属性配置" )
	@PostMapping
	//@PreAuthorize("@per.hasPermission('codegen:templateproperty:add')" )
	public R save(@RequestBody TemplateProperty templateProperty) {
		return templatePropertyService.save(templateProperty) ?
				R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增模板属性配置失败");
	}

	/**
	 * 修改模板属性配置
	 *
	 * @param templateProperty 模板属性配置
	 * @return R
	 */
	@ApiOperation(value = "修改模板属性配置", notes = "修改模板属性配置")
	// @UpdateOperationLogging(msg = "修改模板属性配置" )
	@PutMapping
	//@PreAuthorize("@per.hasPermission('codegen:templateproperty:edit')" )
	public R updateById(@RequestBody TemplateProperty templateProperty) {
		return templatePropertyService.updateById(templateProperty) ?
				R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改模板属性配置失败");
	}

	/**
	 * 通过id删除模板属性配置
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除模板属性配置", notes = "通过id删除模板属性配置")
	// @DeleteOperationLogging(msg = "通过id删除模板属性配置" )
	@DeleteMapping("/{id}")
	// @PreAuthorize("@per.hasPermission('codegen:templateproperty:del')" )
	public R removeById(@PathVariable Integer id) {
		return templatePropertyService.removeById(id) ?
				R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除模板属性配置失败");
	}

}