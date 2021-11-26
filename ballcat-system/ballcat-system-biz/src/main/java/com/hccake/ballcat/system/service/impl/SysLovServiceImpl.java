package com.hccake.ballcat.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.system.event.LovChangeEvent;
import com.hccake.ballcat.system.mapper.SysLovMapper;
import com.hccake.ballcat.system.model.entity.SysLov;
import com.hccake.ballcat.system.model.entity.SysLovBody;
import com.hccake.ballcat.system.model.entity.SysLovSearch;
import com.hccake.ballcat.system.model.qo.SysLovQO;
import com.hccake.ballcat.system.model.vo.SysLovPageVO;
import com.hccake.ballcat.system.service.SysLovBodyService;
import com.hccake.ballcat.system.service.SysLovSearchService;
import com.hccake.ballcat.system.service.SysLovService;
import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.result.BaseResultCode;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lingting 2020-08-10 17:21
 */
@Deprecated
@Service
@ConditionalOnMissingBean(SysLovService.class)
@RequiredArgsConstructor
public class SysLovServiceImpl extends ExtendServiceImpl<SysLovMapper, SysLov> implements SysLovService {

	private final SysLovBodyService bodyService;

	private final SysLovSearchService searchService;

	private final ApplicationEventPublisher eventPublisher;

	@Override
	public PageResult<SysLovPageVO> queryPage(PageParam pageParam, SysLovQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean update(SysLov sysLov, List<SysLovBody> bodyList, List<SysLovSearch> searchList) {
		if (!updateById(sysLov)) {
			return false;
		}
		// 更新 LovBody，先删除再插入
		String keyword = sysLov.getKeyword();
		bodyService.removeByKeyword(keyword);
		bodyList.forEach((body -> body.setKeyword(keyword)));
		bodyService.saveBatch(bodyList);

		// 更新 LovSearch，先删除再插入
		searchService.removeByKeyword(keyword);
		searchList.forEach((body -> body.setKeyword(keyword)));
		searchService.saveBatch(searchList);

		eventPublisher.publishEvent(LovChangeEvent.of(keyword));
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean remove(Integer id) {
		SysLov sysLov = getById(id);
		if (!removeById(id)) {
			throw new BusinessException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "移除lov失败!");
		}
		String keyword = sysLov.getKeyword();
		if (!bodyService.removeByKeyword(keyword)) {
			throw new BusinessException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "移除lovBody失败!");
		}
		if (!searchService.removeByKeyword(keyword)) {
			throw new BusinessException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "移除lovSearch失败!");
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save(SysLov sysLov, List<SysLovBody> bodyList, List<SysLovSearch> searchList) {
		// 1. 保存 lov 主体
		if (!save(sysLov)) {
			throw new BusinessException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "新增lov失败!");
		}

		// 2. 插入body
		if (CollectionUtil.isNotEmpty(bodyList)) {
			bodyList.forEach(body -> body.setKeyword(sysLov.getKeyword()));
			if (!bodyService.saveBatch(bodyList)) {
				throw new BusinessException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "新增lovBody失败!");
			}
		}

		// 3. 插入 search
		if (CollectionUtil.isNotEmpty(searchList)) {
			searchList.forEach(x -> x.setKeyword(sysLov.getKeyword()));
			if (!searchService.saveBatch(searchList)) {
				throw new BusinessException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "新增lovSearch失败!");
			}
		}
		return true;
	}

	/**
	 * 根据keyword获取lov数据
	 * @param keyword keyword
	 * @return Lov
	 */
	@Override
	public SysLov getByKeyword(String keyword) {
		return baseMapper.selectByKeyword(keyword);
	}

	@Override
	public List<String> check(List<SysLov> list) {
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}

		List<String> res = new ArrayList<>(list.size());
		for (SysLov sysLov : list) {
			if (StrUtil.isBlank(sysLov.getKeyword())) {
				continue;
			}

			if (
			// 更新时间为空
			sysLov.getUpdateTime() == null
					// 时间不等
					|| sysLov.getUpdateTime().compareTo(getByKeyword(sysLov.getKeyword()).getUpdateTime()) != 0) {
				res.add(sysLov.getKeyword());
			}
		}

		return res;
	}

}
