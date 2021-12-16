package com.hccake.ballcat.system.controller;

import com.hccake.ballcat.common.log.operation.annotation.CreateOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.DeleteOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.UpdateOperationLogging;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.system.model.entity.SysConfig;
import com.hccake.ballcat.system.model.qo.SysConfigQO;
import com.hccake.ballcat.system.model.vo.SysConfigPageVO;
import com.hccake.ballcat.system.service.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统配置
 *
 * @author ballcat code generator
 * @date 2019-10-14 17:42:23
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/config")
@Tag(name = "系统配置")
public class SysConfigController {

	private final SysConfigService sysConfigService;

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param sysConfigQO 系统配置
	 * @return R<PageResult<SysConfigVO>>
	 */
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('system:config:read')")
	@Operation(summary = "分页查询", description = "分页查询")
	public R<PageResult<SysConfigPageVO>> getSysConfigPage(PageParam pageParam, SysConfigQO sysConfigQO) {
		return R.ok(sysConfigService.queryPage(pageParam, sysConfigQO));
	}

	/**
	 * 新增系统配置
	 * @param sysConfig 系统配置
	 * @return R
	 */
	@CreateOperationLogging(msg = "新增系统配置")
	@PostMapping
	@PreAuthorize("@per.hasPermission('system:config:add')")
	@Operation(summary = "新增系统配置", description = "新增系统配置")
	public R<Boolean> save(@RequestBody SysConfig sysConfig) {
		return R.ok(sysConfigService.save(sysConfig));
	}

	/**
	 * 修改系统配置
	 * @param sysConfig 系统配置
	 * @return R
	 */
	@UpdateOperationLogging(msg = "修改系统配置")
	@PutMapping
	@PreAuthorize("@per.hasPermission('system:config:edit')")
	@Operation(summary = "修改系统配置")
	public R<Boolean> updateById(@RequestBody SysConfig sysConfig) {
		return R.ok(sysConfigService.updateByKey(sysConfig));
	}

	/**
	 * 删除系统配置
	 * @param confKey confKey
	 * @return R
	 */
	@DeleteOperationLogging(msg = "删除系统配置")
	@DeleteMapping
	@PreAuthorize("@per.hasPermission('system:config:del')")
	@Operation(summary = "删除系统配置")
	public R<Boolean> removeById(@RequestParam("confKey") String confKey) {
		return R.ok(sysConfigService.removeByKey(confKey));
	}

}
