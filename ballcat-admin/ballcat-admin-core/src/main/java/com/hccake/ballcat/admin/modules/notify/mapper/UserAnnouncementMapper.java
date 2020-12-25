package com.hccake.ballcat.admin.modules.notify.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hccake.ballcat.admin.modules.notify.model.entity.UserAnnouncement;
import com.hccake.ballcat.admin.modules.notify.model.vo.UserAnnouncementVO;
import org.apache.ibatis.annotations.Param;

/**
 * 用户公告表
 *
 * @author hccake 2020-12-25 08:04:53
 */
public interface UserAnnouncementMapper extends BaseMapper<UserAnnouncement> {

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param wrapper 查询wrapper
	 * @return IPage<UserAnnouncementVO> VO分页数据
	 */
	IPage<UserAnnouncementVO> selectPageVo(IPage<?> page, @Param(Constants.WRAPPER) Wrapper<UserAnnouncement> wrapper);

}