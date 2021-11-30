package com.hccake.ballcat.log.controller;

import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.log.model.qo.OperationLogQO;
import com.hccake.ballcat.log.model.vo.OperationLogPageVO;
import com.hccake.ballcat.log.service.OperationLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作日志
 *
 * @author hccake
 * @date 2019-10-15 20:42:32
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/log/operation-log")
@Api(value = "operation-log", tags = "操作日志管理")
public class OperationLogController {

	private final OperationLogService operationLogService;

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param operationLogQO 操作日志
	 * @return R
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('log:operation-log:read')")
	public R<PageResult<OperationLogPageVO>> getOperationLogAdminPage(PageParam pageParam,
			OperationLogQO operationLogQO) {
		return R.ok(operationLogService.queryPage(pageParam, operationLogQO));
	}

}
