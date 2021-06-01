package com.hccake.ballcat.system.controller;

import com.hccake.ballcat.system.model.dto.SysOrganizationDTO;
import com.hccake.ballcat.system.model.qo.SysOrganizationQO;
import com.hccake.ballcat.system.model.vo.SysOrganizationTree;
import com.hccake.ballcat.system.service.SysOrganizationService;
import com.hccake.ballcat.commom.log.operation.annotation.CreateOperationLogging;
import com.hccake.ballcat.commom.log.operation.annotation.DeleteOperationLogging;
import com.hccake.ballcat.commom.log.operation.annotation.UpdateOperationLogging;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.model.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 组织架构
 *
 * @author hccake 2020-09-23 12:09:43
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/organization")
@Api(value = "system-organization", tags = "组织架构管理")
public class SysOrganizationController {

	private final SysOrganizationService sysOrganizationService;

	/**
	 * 组织架构树查询
	 * @param qo 组织机构查询条件
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "组织架构树查询", notes = "组织架构树查询")
	@GetMapping("/tree")
	@PreAuthorize("@per.hasPermission('system:organization:read')")
	public R<List<SysOrganizationTree>> getOrganizationTree(SysOrganizationQO qo) {
		return R.ok(sysOrganizationService.listTree(qo));
	}

	/**
	 * 新增组织架构
	 * @param sysOrganizationDTO 组织机构DTO
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "新增组织架构", notes = "新增组织架构")
	@CreateOperationLogging(msg = "新增组织架构")
	@PostMapping
	@PreAuthorize("@per.hasPermission('system:organization:add')")
	public R<?> save(@RequestBody SysOrganizationDTO sysOrganizationDTO) {
		return sysOrganizationService.create(sysOrganizationDTO) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增组织架构失败");
	}

	/**
	 * 修改组织架构
	 * @param sysOrganizationDTO 组织机构DTO
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "修改组织架构", notes = "修改组织架构")
	@UpdateOperationLogging(msg = "修改组织架构")
	@PutMapping
	@PreAuthorize("@per.hasPermission('system:organization:edit')")
	public R<?> updateById(@RequestBody SysOrganizationDTO sysOrganizationDTO) {
		return sysOrganizationService.update(sysOrganizationDTO) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改组织架构失败");
	}

	/**
	 * 通过id删除组织架构
	 * @param id id
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "通过id删除组织架构", notes = "通过id删除组织架构")
	@DeleteOperationLogging(msg = "通过id删除组织架构")
	@DeleteMapping("/{id}")
	@PreAuthorize("@per.hasPermission('system:organization:del')")
	public R<?> removeById(@PathVariable("id") Integer id) {
		return sysOrganizationService.removeById(id) ? R.ok()
				: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除组织架构失败");
	}

}