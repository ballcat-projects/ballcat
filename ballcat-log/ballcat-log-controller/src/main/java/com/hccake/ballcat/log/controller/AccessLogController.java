package com.hccake.ballcat.log.controller;

import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.log.model.qo.AccessLogQO;
import com.hccake.ballcat.log.model.vo.AccessLogPageVO;
import com.hccake.ballcat.log.service.AccessLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@ConditionalOnMissingBean(AccessLogController.class)
@RequiredArgsConstructor
@RequestMapping("/log/access-log")
@Api(value = "access-log", tags = "访问日志管理")
public class AccessLogController {

	private final AccessLogService accessLogService;

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param accessLogQO 访问日志查询对象
	 * @return R
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('log:access-log:read')")
	public R<PageResult<AccessLogPageVO>> getAccessLogApiPage(PageParam pageParam, AccessLogQO accessLogQO) {
		return R.ok(accessLogService.queryPage(pageParam, accessLogQO));
	}

}
