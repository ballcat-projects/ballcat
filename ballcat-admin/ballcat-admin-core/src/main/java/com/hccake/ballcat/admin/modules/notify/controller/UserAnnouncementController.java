package com.hccake.ballcat.admin.modules.notify.controller;

import com.hccake.ballcat.admin.modules.notify.model.qo.UserAnnouncementQO;
import com.hccake.ballcat.admin.modules.notify.model.vo.UserAnnouncementPageVO;
import com.hccake.ballcat.admin.modules.notify.service.UserAnnouncementService;
import com.hccake.ballcat.admin.oauth.util.SecurityUtils;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户公告表
 *
 * @author hccake 2020-12-25 08:04:53
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/notify/user-announcement")
@Api(value = "user-announcement", tags = "用户公告表管理")
public class UserAnnouncementController {

	private final UserAnnouncementService userAnnouncementService;

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param userAnnouncementQO 用户公告表查询对象
	 * @return R 通用返回体
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@per.hasPermission('notify:userannouncement:read')")
	public R<PageResult<UserAnnouncementPageVO>> getUserAnnouncementPage(PageParam pageParam,
			UserAnnouncementQO userAnnouncementQO) {
		return R.ok(userAnnouncementService.queryPage(pageParam, userAnnouncementQO));
	}

	@ApiOperation(value = "用户公告已读上报", notes = "用户公告已读上报")
	@PatchMapping("/read/{announcementId}")
	@PreAuthorize("@per.hasPermission('notify:userannouncement:read')")
	public R<?> readAnnouncement(@PathVariable Long announcementId) {
		Integer userId = SecurityUtils.getSysUser().getUserId();
		userAnnouncementService.readAnnouncement(userId, announcementId);
		return R.ok();
	}

}