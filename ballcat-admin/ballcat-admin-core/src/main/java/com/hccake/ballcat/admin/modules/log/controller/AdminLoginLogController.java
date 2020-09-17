package com.hccake.ballcat.admin.modules.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hccake.ballcat.admin.modules.log.model.qo.AdminLoginLogQO;
import com.hccake.ballcat.admin.modules.log.model.vo.AdminLoginLogVO;
import com.hccake.ballcat.admin.modules.log.service.AdminLoginLogService;
import com.hccake.ballcat.common.core.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登陆日志
 *
 * @author hccake 2020-09-16 20:21:10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/log/adminloginlog")
@Api(value = "adminloginlog", tags = "登陆日志管理")
public class AdminLoginLogController {

	private final AdminLoginLogService AdminLoginLogService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param adminLoginLogQO 登陆日志查询对象
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('log:adminloginlog:read')")
	public R<IPage<AdminLoginLogVO>> getLoginLogPage(Page<?> page, AdminLoginLogQO adminLoginLogQO) {
		return R.ok(AdminLoginLogService.selectPageVo(page, adminLoginLogQO));
	}

}