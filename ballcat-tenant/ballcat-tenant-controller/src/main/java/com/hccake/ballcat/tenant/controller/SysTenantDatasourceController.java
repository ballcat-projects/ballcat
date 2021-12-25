package com.hccake.ballcat.tenant.controller;

import com.hccake.ballcat.common.log.operation.annotation.CreateOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.DeleteOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.UpdateOperationLogging;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.tenant.model.entity.SysTenantDatasource;
import com.hccake.ballcat.tenant.model.qo.SysTenantDatasourceQO;
import com.hccake.ballcat.tenant.model.vo.SysTenantDatasourcePageVO;
import com.hccake.ballcat.tenant.service.SysTenantDatasourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 租户数据源映射表
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tenant/sys-tenant-datasource")
@Api(value = "sys-tenant-datasource", tags = "租户数据源映射表管理")
public class SysTenantDatasourceController {

	private final SysTenantDatasourceService sysTenantDatasourceService;

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param sysTenantDatasourceQO 租户数据源映射表查询对象
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('tenant:sys-tenant-datasource:read')")
	public R<PageResult<SysTenantDatasourcePageVO>> getSysTenantDatasourcePage(PageParam pageParam,
			SysTenantDatasourceQO sysTenantDatasourceQO) {
		return R.ok(sysTenantDatasourceService.queryPage(pageParam, sysTenantDatasourceQO));
	}

	/**
	 * 新增租户数据源映射表
	 * @param sysTenantDatasource 租户数据源映射表
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "新增租户数据源映射表", notes = "新增租户数据源映射表")
	@CreateOperationLogging(msg = "新增租户数据源映射表")
	@PostMapping
	@PreAuthorize("@per.hasPermission('tenant:sys-tenant-datasource:add')")
	public R save(@RequestBody SysTenantDatasource sysTenantDatasource) {
		return sysTenantDatasourceService.save(sysTenantDatasource) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增租户数据源映射表失败");
	}

	/**
	 * 修改租户数据源映射表
	 * @param sysTenantDatasource 租户数据源映射表
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "修改租户数据源映射表", notes = "修改租户数据源映射表")
	@UpdateOperationLogging(msg = "修改租户数据源映射表")
	@PutMapping
	@PreAuthorize("@per.hasPermission('tenant:sys-tenant-datasource:edit')")
	public R updateById(@RequestBody SysTenantDatasource sysTenantDatasource) {
		return sysTenantDatasourceService.updateById(sysTenantDatasource) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改租户数据源映射表失败");
	}

	/**
	 * 通过id删除租户数据源映射表
	 * @param id id
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "通过id删除租户数据源映射表", notes = "通过id删除租户数据源映射表")
	@DeleteOperationLogging(msg = "通过id删除租户数据源映射表")
	@DeleteMapping("/{id}")
	@PreAuthorize("@per.hasPermission('tenant:sys-tenant-datasource:del')")
	public R removeById(@PathVariable("id") Long id) {
		return sysTenantDatasourceService.removeById(id) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除租户数据源映射表失败");
	}

}