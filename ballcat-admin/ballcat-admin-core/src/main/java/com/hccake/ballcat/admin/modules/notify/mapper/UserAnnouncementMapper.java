package com.hccake.ballcat.admin.modules.notify.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.admin.constants.UserAnnouncementStateEnum;
import com.hccake.ballcat.admin.modules.notify.converter.UserAnnouncementConverter;
import com.hccake.ballcat.admin.modules.notify.model.entity.UserAnnouncement;
import com.hccake.ballcat.admin.modules.notify.model.qo.UserAnnouncementQO;
import com.hccake.ballcat.admin.modules.notify.model.vo.UserAnnouncementPageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

import java.time.LocalDateTime;

/**
 * 用户公告表
 *
 * @author hccake 2020-12-25 08:04:53
 */
public interface UserAnnouncementMapper extends ExtendMapper<UserAnnouncement> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return 分页结果数据 PageResult
	 */
	default PageResult<UserAnnouncementPageVO> queryPage(PageParam pageParam, UserAnnouncementQO qo) {
		IPage<UserAnnouncement> page = this.prodPage(pageParam);
		LambdaQueryWrapperX<UserAnnouncement> wrapperX = WrappersX.lambdaAliasQueryX(UserAnnouncement.class)
				.eqIfPresent(UserAnnouncement::getId, qo.getId());
		this.selectPage(page, wrapperX);
		IPage<UserAnnouncementPageVO> voPage = page.convert(UserAnnouncementConverter.INSTANCE::poToPageVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	/**
	 * 更新用户公共信息至已读状态
	 * @param userId 用户ID
	 * @param announcementId 公告ID
	 */
	default void updateToReadState(Integer userId, Long announcementId) {
		LambdaUpdateWrapper<UserAnnouncement> wrapper = Wrappers.<UserAnnouncement>lambdaUpdate()
				.set(UserAnnouncement::getState, UserAnnouncementStateEnum.READ.getValue())
				.set(UserAnnouncement::getReadTime, LocalDateTime.now())
				.eq(UserAnnouncement::getAnnouncementId, announcementId).eq(UserAnnouncement::getUserId, userId);
		this.update(null, wrapper);
	}

}