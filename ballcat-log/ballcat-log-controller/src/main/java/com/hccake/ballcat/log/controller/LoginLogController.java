package com.hccake.ballcat.log.controller;

import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.log.model.qo.LoginLogQO;
import com.hccake.ballcat.log.model.vo.LoginLogPageVO;
import com.hccake.ballcat.log.service.LoginLogService;
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
@RequestMapping("/log/login-log")
@Api(value = "login-log", tags = "登陆日志管理")
public class LoginLogController {

	private final LoginLogService loginLogService;

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param loginLogQO 登陆日志查询对象
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('log:login-log:read')")
	public R<PageResult<LoginLogPageVO>> getLoginLogPage(PageParam pageParam, LoginLogQO loginLogQO) {
		return R.ok(loginLogService.queryPage(pageParam, loginLogQO));
	}

}