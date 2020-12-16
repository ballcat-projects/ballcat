package com.hccake.ballcat.admin.modules.notify.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.ballcat.admin.modules.notify.mapper.AnnouncementMapper;
import com.hccake.ballcat.admin.modules.notify.model.entity.Announcement;
import com.hccake.ballcat.admin.modules.notify.model.qo.AnnouncementQO;
import com.hccake.ballcat.admin.modules.notify.model.vo.AnnouncementVO;
import com.hccake.ballcat.admin.modules.notify.service.AnnouncementService;
import org.springframework.stereotype.Service;

/**
 * 公告信息
 *
 * @author hccake 2020-12-15 17:01:15
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement>
		implements AnnouncementService {

	private final static String TABLE_ALIAS_PREFIX = "a.";

	/**
	 * 根据QueryObeject查询分页数据
	 * @param page 分页参数
	 * @param qo 查询参数对象
	 * @return IPage<AnnouncementVO> 分页数据
	 */
	@Override
	public IPage<AnnouncementVO> selectPageVo(IPage<?> page, AnnouncementQO qo) {
		QueryWrapper<Announcement> wrapper = Wrappers.<Announcement>query()
				.like(StrUtil.isNotBlank(qo.getTitle()), TABLE_ALIAS_PREFIX + "title", qo.getTitle())
				.eq(ObjectUtil.isNotNull(qo.getRecipientFilterType()), TABLE_ALIAS_PREFIX + "recipient_filter_type",
						qo.getRecipientFilterType());
		return baseMapper.selectPageVo(page, wrapper);
	}

}
