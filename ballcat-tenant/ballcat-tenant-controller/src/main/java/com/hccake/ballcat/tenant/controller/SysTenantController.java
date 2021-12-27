package com.hccake.ballcat.tenant.controller;

import com.hccake.ballcat.common.log.operation.annotation.CreateOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.DeleteOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.UpdateOperationLogging;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.tenant.model.entity.SysTenant;
import com.hccake.ballcat.tenant.model.qo.SysTenantQO;
import com.hccake.ballcat.tenant.model.vo.SysTenantPageVO;
import com.hccake.ballcat.tenant.service.SysTenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 租户表
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tenant/sys-tenant")
@Api(value = "sys-tenant", tags = "租户表管理")
public class SysTenantController {

	private final SysTenantService sysTenantService;

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param sysTenantQO 租户表查询对象
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('tenant:sys-tenant:read')")
	public R<PageResult<SysTenantPageVO>> getSysTenantPage(PageParam pageParam, SysTenantQO sysTenantQO) {
		return R.ok(sysTenantService.queryPage(pageParam, sysTenantQO));
	}

	/**
	 * 新增租户表
	 * @param sysTenant 租户表
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "新增租户表", notes = "新增租户表")
	@CreateOperationLogging(msg = "新增租户表")
	@PostMapping
	@PreAuthorize("@per.hasPermission('tenant:sys-tenant:add')")
	public R save(@RequestBody SysTenant sysTenant) {
		return sysTenantService.save(sysTenant) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增租户表失败");
	}

	/**
	 * 修改租户表
	 * @param sysTenant 租户表
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "修改租户表", notes = "修改租户表")
	@UpdateOperationLogging(msg = "修改租户表")
	@PutMapping
	@PreAuthorize("@per.hasPermission('tenant:sys-tenant:edit')")
	public R updateById(@RequestBody SysTenant sysTenant) {
		return sysTenantService.updateById(sysTenant) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改租户表失败");
	}

	/**
	 * 通过id删除租户表
	 * @param tenantId id
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "通过id删除租户表", notes = "通过id删除租户表")
	@DeleteOperationLogging(msg = "通过id删除租户表")
	@DeleteMapping("/{tenantId}")
	@PreAuthorize("@per.hasPermission('tenant:sys-tenant:del')")
	public R removeById(@PathVariable("tenantId") Long tenantId) {
		return sysTenantService.removeById(tenantId) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除租户表失败");
	}

}