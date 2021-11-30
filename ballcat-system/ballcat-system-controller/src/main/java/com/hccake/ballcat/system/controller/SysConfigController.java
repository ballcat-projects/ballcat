package com.hccake.ballcat.system.controller;

import com.hccake.ballcat.system.model.entity.SysConfig;
import com.hccake.ballcat.system.model.qo.SysConfigQO;
import com.hccake.ballcat.system.model.vo.SysConfigPageVO;
import com.hccake.ballcat.system.service.SysConfigService;
import com.hccake.ballcat.common.log.operation.annotation.CreateOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.DeleteOperationLogging;
import com.hccake.ballcat.common.log.operation.annotation.UpdateOperationLogging;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 系统配置
 *
 * @author ballcat code generator
 * @date 2019-10-14 17:42:23
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/config")
@Api(value = "system-config", tags = "系统配置")
public class SysConfigController {

	private final SysConfigService sysConfigService;

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param sysConfigQO 系统配置
	 * @return R<PageResult<SysConfigVO>>
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('system:config:read')")
	public R<PageResult<SysConfigPageVO>> getSysConfigPage(PageParam pageParam, SysConfigQO sysConfigQO) {
		return R.ok(sysConfigService.queryPage(pageParam, sysConfigQO));
	}

	/**
	 * 新增系统配置
	 * @param sysConfig 系统配置
	 * @return R
	 */
	@ApiOperation(value = "新增系统配置", notes = "新增系统配置")
	@CreateOperationLogging(msg = "新增系统配置")
	@PostMapping
	@PreAuthorize("@per.hasPermission('system:config:add')")
	public R save(@RequestBody SysConfig sysConfig) {
		return R.ok(sysConfigService.save(sysConfig));
	}

	/**
	 * 修改系统配置
	 * @param sysConfig 系统配置
	 * @return R
	 */
	@ApiOperation(value = "修改系统配置")
	@UpdateOperationLogging(msg = "修改系统配置")
	@PutMapping
	@PreAuthorize("@per.hasPermission('system:config:edit')")
	public R updateById(@RequestBody SysConfig sysConfig) {
		return R.ok(sysConfigService.updateByKey(sysConfig));
	}

	/**
	 * 删除系统配置
	 * @param confKey confKey
	 * @return R
	 */
	@ApiOperation(value = "删除系统配置")
	@DeleteOperationLogging(msg = "删除系统配置")
	@DeleteMapping
	@PreAuthorize("@per.hasPermission('system:config:del')")
	public R removeById(@RequestParam("confKey") String confKey) {
		return R.ok(sysConfigService.removeByKey(confKey));
	}

}
