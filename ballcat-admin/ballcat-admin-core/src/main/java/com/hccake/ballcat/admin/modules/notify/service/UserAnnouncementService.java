package com.hccake.ballcat.admin.modules.notify.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hccake.ballcat.admin.modules.notify.model.entity.UserAnnouncement;
import com.hccake.ballcat.admin.modules.notify.model.vo.UserAnnouncementVO;
import com.hccake.ballcat.admin.modules.notify.model.qo.UserAnnouncementQO;

/**
 * 用户公告表
 *
 * @author hccake 2020-12-25 08:04:53
 */
public interface UserAnnouncementService extends IService<UserAnnouncement> {

	/**
	 * 根据QueryObeject查询分页数据
	 * @param page 分页参数
	 * @param qo 查询参数对象
	 * @return IPage<UserAnnouncementVO> 分页数据
	 */
	IPage<UserAnnouncementVO> selectPageVo(IPage<?> page, UserAnnouncementQO qo);

}