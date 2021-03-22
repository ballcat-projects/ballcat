package com.hccake.ballcat.admin.modules.notify.service;

import com.hccake.ballcat.admin.modules.notify.model.entity.UserAnnouncement;
import com.hccake.ballcat.admin.modules.notify.model.qo.UserAnnouncementQO;
import com.hccake.ballcat.admin.modules.notify.model.vo.UserAnnouncementPageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;

/**
 * 用户公告表
 *
 * @author hccake 2020-12-25 08:04:53
 */
public interface UserAnnouncementService extends ExtendService<UserAnnouncement> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<AnnouncementVO> 分页数据
	 */
	PageResult<UserAnnouncementPageVO> queryPage(PageParam pageParam, UserAnnouncementQO qo);

	/**
	 * 根据用户ID和公告id初始化一个新的用户公告关联对象
	 * @param userId 用户ID
	 * @param announcementId 公告ID
	 * @return UserAnnouncement
	 */
	UserAnnouncement prodUserAnnouncement(Integer userId, Long announcementId);

	/**
	 * 对用户公告进行已读标记
	 * @param userId 用户id
	 * @param announcementId 公告id
	 */
	void readAnnouncement(Integer userId, Long announcementId);

}