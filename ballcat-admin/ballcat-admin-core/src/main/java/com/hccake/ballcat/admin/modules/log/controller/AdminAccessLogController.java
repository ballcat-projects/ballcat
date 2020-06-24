package com.hccake.ballcat.admin.modules.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hccake.ballcat.admin.modules.log.model.entity.AdminAccessLog;
import com.hccake.ballcat.admin.modules.log.service.AdminAccessLogService;
import com.hccake.ballcat.common.core.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 访问日志
 *
 * @author hccake
 * @date 2019-10-16 16:09:25
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/log/adminaccesslog")
@Api(value = "adminaccesslog", tags = "访问日志管理")
public class AdminAccessLogController {

	private final AdminAccessLogService adminAccessLogService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param adminAccessLog 访问日志
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('log:adminaccesslog:read')")
	public R<IPage<AdminAccessLog>> getAccessLogApiPage(Page<AdminAccessLog> page, AdminAccessLog adminAccessLog) {
		return R.ok(adminAccessLogService.page(page, Wrappers.query(adminAccessLog)));
	}

	/**
	 * 通过id查询访问日志
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询后台访问日志", notes = "通过id查询后台访问日志")
	@GetMapping("/{id}")
	@PreAuthorize("@per.hasPermission('log:adminaccesslog:read')")
	public R<AdminAccessLog> getById(@PathVariable("id") Long id) {
		return R.ok(adminAccessLogService.getById(id));
	}

}
