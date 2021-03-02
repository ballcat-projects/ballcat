package com.hccake.ballcat.admin.modules.log.controller;

import com.hccake.ballcat.admin.modules.log.model.qo.AdminAccessLogQO;
import com.hccake.ballcat.admin.modules.log.model.vo.AdminAccessLogVO;
import com.hccake.ballcat.admin.modules.log.service.AdminAccessLogService;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
	 * @param pageParam 分页参数
	 * @param adminAccessLogQO 访问日志查询对象
	 * @return R
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('log:adminaccesslog:read')")
	public R<PageResult<AdminAccessLogVO>> getAccessLogApiPage(PageParam pageParam, AdminAccessLogQO adminAccessLogQO) {
		return R.ok(adminAccessLogService.queryPage(pageParam, adminAccessLogQO));
	}

}
