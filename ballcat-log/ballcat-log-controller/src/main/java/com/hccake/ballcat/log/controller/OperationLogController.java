package com.hccake.ballcat.log.controller;

import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.log.model.qo.OperationLogQO;
import com.hccake.ballcat.log.model.vo.OperationLogPageVO;
import com.hccake.ballcat.log.service.OperationLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "操作日志管理")
public class OperationLogController {

	private final OperationLogService operationLogService;

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param operationLogQO 操作日志
	 * @return R
	 */
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('log:operation-log:read')")
	@Operation(summary = "分页查询", description = "分页查询")
	public R<PageResult<OperationLogPageVO>> getOperationLogAdminPage(PageParam pageParam,
			OperationLogQO operationLogQO) {
		return R.ok(operationLogService.queryPage(pageParam, operationLogQO));
	}

}
