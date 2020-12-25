package com.hccake.ballcat.admin.modules.notify.model.converter;

import com.hccake.ballcat.admin.modules.notify.model.dto.AnnouncementDTO;
import com.hccake.ballcat.admin.modules.notify.model.entity.Announcement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Hccake 2020/12/16
 * @version 1.0
 */
@Mapper
public interface AnnouncementConverter {

	AnnouncementConverter INSTANCE = Mappers.getMapper(AnnouncementConverter.class);

	/**
	 * AnnouncementDTO 转 Announcement实体
	 * @param dto AnnouncementDTO
	 * @return Announcement
	 */
	@Mapping(target = "updateTime", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "createBy", ignore = true)
	Announcement dtoToPo(AnnouncementDTO dto);

}
