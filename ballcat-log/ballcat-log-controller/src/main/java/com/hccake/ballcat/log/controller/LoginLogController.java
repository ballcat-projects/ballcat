package com.hccake.ballcat.log.controller;

import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.log.model.qo.LoginLogQO;
import com.hccake.ballcat.log.model.vo.LoginLogPageVO;
import com.hccake.ballcat.log.service.LoginLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/log/login-log")
@Tag(name = "登陆日志管理")
public class LoginLogController {

	private final LoginLogService loginLogService;

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param loginLogQO 登陆日志查询对象
	 * @return R 通用返回体
	 */
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('log:login-log:read')")
	@Operation(summary = "分页查询", description = "分页查询")
	public R<PageResult<LoginLogPageVO>> getLoginLogPage(PageParam pageParam, LoginLogQO loginLogQO) {
		return R.ok(loginLogService.queryPage(pageParam, loginLogQO));
	}

}