package com.hccake.ballcat.tenant.controller;

import com.hccake.ballcat.common.log.operation.annotation.CreateOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.DeleteOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.UpdateOperationLogging;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.tenant.model.entity.SysDatasource;
import com.hccake.ballcat.tenant.model.qo.SysDatasourceQO;
import com.hccake.ballcat.tenant.model.vo.SysDatasourcePageVO;
import com.hccake.ballcat.tenant.service.SysDatasourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 数据源表
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tenant/sys-datasource")
@Api(value = "sys-datasource", tags = "数据源表管理")
public class SysDatasourceController {

	private final SysDatasourceService sysDatasourceService;

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param sysDatasourceQO 数据源表查询对象
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('tenant:sys-datasource:read')")
	public R<PageResult<SysDatasourcePageVO>> getSysDatasourcePage(PageParam pageParam,
			SysDatasourceQO sysDatasourceQO) {
		return R.ok(sysDatasourceService.queryPage(pageParam, sysDatasourceQO));
	}

	/**
	 * 新增数据源表
	 * @param sysDatasource 数据源表
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "新增数据源表", notes = "新增数据源表")
	@CreateOperationLogging(msg = "新增数据源表")
	@PostMapping
	@PreAuthorize("@per.hasPermission('tenant:sys-datasource:add')")
	public R save(@RequestBody SysDatasource sysDatasource) {
		return sysDatasourceService.save(sysDatasource) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增数据源表失败");
	}

	/**
	 * 修改数据源表
	 * @param sysDatasource 数据源表
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "修改数据源表", notes = "修改数据源表")
	@UpdateOperationLogging(msg = "修改数据源表")
	@PutMapping
	@PreAuthorize("@per.hasPermission('tenant:sys-datasource:edit')")
	public R updateById(@RequestBody SysDatasource sysDatasource) {
		return sysDatasourceService.updateById(sysDatasource) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改数据源表失败");
	}

	/**
	 * 通过id删除数据源表
	 * @param id id
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "通过id删除数据源表", notes = "通过id删除数据源表")
	@DeleteOperationLogging(msg = "通过id删除数据源表")
	@DeleteMapping("/{id}")
	@PreAuthorize("@per.hasPermission('tenant:sys-datasource:del')")
	public R removeById(@PathVariable("id") Long id) {
		return sysDatasourceService.removeById(id) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除数据源表失败");
	}

}