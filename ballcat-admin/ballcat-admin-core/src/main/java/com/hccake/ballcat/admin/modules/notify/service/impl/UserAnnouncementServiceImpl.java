package com.hccake.ballcat.admin.modules.notify.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.ballcat.admin.modules.notify.model.entity.UserAnnouncement;
import com.hccake.ballcat.admin.modules.notify.model.vo.UserAnnouncementVO;
import com.hccake.ballcat.admin.modules.notify.model.qo.UserAnnouncementQO;
import com.hccake.ballcat.admin.modules.notify.mapper.UserAnnouncementMapper;
import com.hccake.ballcat.admin.modules.notify.service.UserAnnouncementService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户公告表
 *
 * @author hccake 2020-12-25 08:04:53
 */
@Service
public class UserAnnouncementServiceImpl extends ServiceImpl<UserAnnouncementMapper, UserAnnouncement>
		implements UserAnnouncementService {

	private final static String TABLE_ALIAS_PREFIX = "ua.";

	/**
	 * 根据QueryObeject查询分页数据
	 * @param page 分页参数
	 * @param qo 查询参数对象
	 * @return IPage<UserAnnouncementVO> 分页数据
	 */
	@Override
	public IPage<UserAnnouncementVO> selectPageVo(IPage<?> page, UserAnnouncementQO qo) {
		QueryWrapper<UserAnnouncement> wrapper = Wrappers.<UserAnnouncement>query().eq(ObjectUtil.isNotNull(qo.getId()),
				TABLE_ALIAS_PREFIX + "id", qo.getId());
		return baseMapper.selectPageVo(page, wrapper);
	}

	/**
	 * 根据用户ID和公告id初始化一个新的用户公告关联对象
	 * @param userId 用户ID
	 * @param announcementId 公告ID
	 * @return UserAnnouncement
	 */
	@Override
	public UserAnnouncement prodUserAnnouncement(Integer userId, Long announcementId) {
		UserAnnouncement userAnnouncement = new UserAnnouncement();
		userAnnouncement.setUserId(userId);
		userAnnouncement.setAnnouncementId(announcementId);
		userAnnouncement.setCreateTime(LocalDateTime.now());
		userAnnouncement.setState(0);
		return userAnnouncement;
	}

}
