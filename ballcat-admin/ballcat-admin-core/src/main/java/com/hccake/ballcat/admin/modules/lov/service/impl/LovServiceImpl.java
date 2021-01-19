package com.hccake.ballcat.admin.modules.lov.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.admin.modules.lov.mapper.LovMapper;
import com.hccake.ballcat.admin.modules.lov.model.entity.Lov;
import com.hccake.ballcat.admin.modules.lov.model.entity.LovBody;
import com.hccake.ballcat.admin.modules.lov.model.entity.LovSearch;
import com.hccake.ballcat.admin.modules.lov.model.qo.LovQO;
import com.hccake.ballcat.admin.modules.lov.model.vo.LovVO;
import com.hccake.ballcat.admin.modules.lov.service.LovBodyService;
import com.hccake.ballcat.admin.modules.lov.service.LovSearchService;
import com.hccake.ballcat.admin.modules.lov.service.LovService;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.domain.PageResult;
import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.ballcat.common.core.result.BaseResultCode;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lingting 2020-08-10 17:21
 */
@Service
@RequiredArgsConstructor
public class LovServiceImpl extends ExtendServiceImpl<LovMapper, Lov> implements LovService {

	private final LovBodyService bodyService;

	private final LovSearchService searchService;

	@Override
	public PageResult<LovVO> queryPage(PageParam pageParam, LovQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean update(Lov lov, List<LovBody> bodyList, List<LovSearch> searchList) {
		if (!updateById(lov)) {
			return false;
		}
		List<Long> removeIds = new ArrayList<>();
		// 获取现有lov body
		String keyword = lov.getKeyword();
		List<LovBody> lovBodyList = bodyService.listByKeyword(keyword);

		// 获取现有的id
		Set<Long> ids = bodyList.stream().map(LovBody::getId).collect(Collectors.toSet());
		// 筛选需要删除的id
		for (LovBody body : lovBodyList) {
			if (!ids.contains(body.getId())) {
				removeIds.add(body.getId());
			}
		}
		bodyService.removeByIds(removeIds);

		bodyList.forEach((body -> body.setKeyword(keyword)));
		bodyService.saveOrUpdateBatch(bodyList);

		// 清空已有需要删除的id
		removeIds.clear();
		// 获取现有lov body
		List<LovSearch> lovSearchList = searchService.listByKeyword(keyword);

		// 获取现有的id
		ids = searchList.stream().map(LovSearch::getId).collect(Collectors.toSet());
		// 筛选需要删除的id
		for (LovSearch search : lovSearchList) {
			if (!ids.contains(search.getId())) {
				removeIds.add(search.getId());
			}
		}
		searchService.removeByIds(removeIds);

		searchList.forEach((body -> body.setKeyword(keyword)));
		searchService.saveOrUpdateBatch(searchList);
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean remove(Integer id) {
		Lov lov = getById(id);
		if (!removeById(id)) {
			throw new BusinessException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "移除lov失败!");
		}
		String keyword = lov.getKeyword();
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
	public boolean save(Lov lov, List<LovBody> bodyList, List<LovSearch> searchList) {
		// 1. 保存 lov 主体
		if (!save(lov)) {
			throw new BusinessException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "新增lov失败!");
		}

		// 2. 插入body
		if (CollectionUtil.isNotEmpty(bodyList)) {
			bodyList.forEach(body -> body.setKeyword(lov.getKeyword()));
			if (!bodyService.saveBatchSomeColumn(bodyList)) {
				throw new BusinessException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "新增lovBody失败!");
			}
		}

		// 3. 插入 search
		if (CollectionUtil.isNotEmpty(searchList)) {
			searchList.forEach(x -> x.setKeyword(lov.getKeyword()));
			if (!searchService.saveBatchSomeColumn(searchList)) {
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
	public Lov getByKeyword(String keyword) {
		return baseMapper.selectByKeyword(keyword);
	}

}
