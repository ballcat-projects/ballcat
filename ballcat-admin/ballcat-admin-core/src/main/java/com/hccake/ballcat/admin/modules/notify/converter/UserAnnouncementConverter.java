package com.hccake.ballcat.admin.modules.notify.converter;

import com.hccake.ballcat.admin.modules.notify.model.entity.UserAnnouncement;
import com.hccake.ballcat.admin.modules.notify.model.vo.UserAnnouncementPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 用户公告表
 *
 * @author hccake 2021-03-22 20:16:12
 */
@Mapper
public interface UserAnnouncementConverter {

	UserAnnouncementConverter INSTANCE = Mappers.getMapper(UserAnnouncementConverter.class);

	/**
	 * PO 转 PageVO
	 * @param userAnnouncement 用户公告表
	 * @return UserAnnouncementPageVO 用户公告表PageVO
	 */
	UserAnnouncementPageVO poToPageVo(UserAnnouncement userAnnouncement);

}
