package com.hccake.ballcat.admin.modules.notify.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.admin.constants.AnnouncementStatusEnum;
import com.hccake.ballcat.admin.modules.notify.converter.AnnouncementConverter;
import com.hccake.ballcat.admin.modules.notify.converter.NotifyInfoConverter;
import com.hccake.ballcat.admin.modules.notify.event.AnnouncementCloseEvent;
import com.hccake.ballcat.admin.modules.notify.event.NotifyPublishEvent;
import com.hccake.ballcat.admin.modules.notify.mapper.AnnouncementMapper;
import com.hccake.ballcat.admin.modules.notify.model.domain.NotifyInfo;
import com.hccake.ballcat.admin.modules.notify.model.dto.AnnouncementDTO;
import com.hccake.ballcat.admin.modules.notify.model.entity.Announcement;
import com.hccake.ballcat.admin.modules.notify.model.qo.AnnouncementQO;
import com.hccake.ballcat.admin.modules.notify.model.vo.AnnouncementPageVO;
import com.hccake.ballcat.admin.modules.notify.service.AnnouncementService;
import com.hccake.ballcat.common.core.constant.enums.BooleanEnum;
import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.ballcat.common.model.result.SystemResultCode;
import com.hccake.ballcat.file.service.FileService;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 公告信息
 *
 * @author hccake 2020-12-15 17:01:15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl extends ExtendServiceImpl<AnnouncementMapper, Announcement>
		implements AnnouncementService {

	private final ApplicationEventPublisher publisher;

	private final FileService fileService;

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<AnnouncementVO> 分页数据
	 */
	@Override
	public PageResult<AnnouncementPageVO> queryPage(PageParam pageParam, AnnouncementQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	/**
	 * 创建公告
	 * @param announcementDTO 公告信息
	 * @return boolean
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean addAnnouncement(AnnouncementDTO announcementDTO) {
		Announcement announcement = AnnouncementConverter.INSTANCE.dtoToPo(announcementDTO);
		announcement.setId(null);
		int flag = baseMapper.insert(announcement);
		boolean inserted = SqlHelper.retBool(flag);
		// 公告发布事件
		boolean isPublishStatus = announcement.getStatus() == AnnouncementStatusEnum.ENABLED.getValue();
		if (inserted && isPublishStatus) {
			this.onAnnouncementPublish(announcement);
		}
		return inserted;
	}

	/**
	 * 更新公告信息
	 * @param announcementDTO announcementDTO
	 * @return boolean
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateAnnouncement(AnnouncementDTO announcementDTO) {
		Announcement oldAnnouncement = baseMapper.selectById(announcementDTO.getId());
		if (oldAnnouncement.getStatus() != AnnouncementStatusEnum.UNPUBLISHED.getValue()) {
			throw new BusinessException(SystemResultCode.BAD_REQUEST.getCode(), "不允许修改已经发布过的公告！");
		}

		Announcement announcement = AnnouncementConverter.INSTANCE.dtoToPo(announcementDTO);
		// 不允许修改为《发布中》以外的状态
		boolean isPublishStatus = announcement.getStatus() == AnnouncementStatusEnum.ENABLED.getValue();
		if (!isPublishStatus) {
			announcement.setStatus(null);
		}
		// 保证当前状态未被修改过
		boolean isUpdated = baseMapper.updateIfUnpublished(announcement);
		// 公告发布事件
		if (isUpdated && isPublishStatus) {
			this.onAnnouncementPublish(announcement);
		}
		return isUpdated;
	}

	/**
	 * 发布公告信息
	 * @param announcementId 公告ID
	 * @return boolean
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean publish(Long announcementId) {
		Announcement announcement = baseMapper.selectById(announcementId);
		if (announcement.getStatus() != AnnouncementStatusEnum.UNPUBLISHED.getValue()) {
			throw new BusinessException(SystemResultCode.BAD_REQUEST.getCode(), "不允许修改已经发布过的公告！");
		}
		if (BooleanEnum.TRUE.getValue() != announcement.getImmortal()
				&& LocalDateTime.now().isAfter(announcement.getDeadline())) {
			throw new BusinessException(SystemResultCode.BAD_REQUEST.getCode(), "公告失效时间必须迟于当前时间！");
		}

		// 更新公共至发布状态
		Announcement entity = new Announcement();
		entity.setId(announcementId);
		entity.setStatus(AnnouncementStatusEnum.ENABLED.getValue());
		boolean isUpdated = baseMapper.updateIfUnpublished(entity);
		if (isUpdated) {
			announcement.setStatus(AnnouncementStatusEnum.ENABLED.getValue());
			this.onAnnouncementPublish(announcement);
		}
		return isUpdated;
	}

	/**
	 * 关闭公告信息
	 * @param announcementId 公告ID
	 * @return boolean
	 */
	@Override
	public boolean close(Long announcementId) {
		Announcement announcement = new Announcement();
		announcement.setId(announcementId);
		announcement.setStatus(AnnouncementStatusEnum.DISABLED.getValue());
		int flag = baseMapper.updateById(announcement);
		boolean isUpdated = SqlHelper.retBool(flag);
		if (isUpdated) {
			publisher.publishEvent(new AnnouncementCloseEvent(announcementId));
		}
		return isUpdated;
	}

	/**
	 * 批量上传公告图片
	 * @param files 图片文件
	 * @return 上传后的图片相对路径集合
	 */
	@Override
	public List<String> uploadImages(List<MultipartFile> files) {
		List<String> objectNames = new ArrayList<>();
		for (MultipartFile file : files) {
			String objectName = "announcement/" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
					+ StrUtil.SLASH + IdUtil.fastSimpleUUID() + StrUtil.DOT
					+ FileUtil.extName(file.getOriginalFilename());
			try {
				objectName = fileService.upload(file.getInputStream(), objectName, file.getSize());
				objectNames.add(objectName);
			}
			catch (IOException e) {
				// TODO 删除无效文件
				throw new BusinessException(BaseResultCode.FILE_UPLOAD_ERROR.getCode(), "图片上传失败！", e);
			}
		}
		return objectNames;
	}

	/**
	 * 当前用户未拉取过的发布中，且满足失效时间的公告信息
	 * @return List<Announcement>
	 */
	@Override
	public List<Announcement> listUnPulled(Integer userId) {
		return baseMapper.listUserAnnouncements(userId, false);
	}

	/**
	 * 获取用户拉取过的发布中，且满足失效时间的公告信息
	 * @param userId 用户id
	 * @return List<Announcement>
	 */
	@Override
	public List<Announcement> listActiveAnnouncements(Integer userId) {
		return baseMapper.listUserAnnouncements(userId, true);
	}

	/**
	 * 公告发布事件
	 * @param announcement 公告信息
	 */
	private void onAnnouncementPublish(Announcement announcement) {
		NotifyInfo notifyInfo = NotifyInfoConverter.INSTANCE.fromAnnouncement(announcement);
		publisher.publishEvent(new NotifyPublishEvent(notifyInfo));
	}

}
