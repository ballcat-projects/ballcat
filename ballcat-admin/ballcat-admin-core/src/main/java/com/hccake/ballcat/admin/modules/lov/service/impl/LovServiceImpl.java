package com.hccake.ballcat.admin.modules.lov.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.ballcat.admin.modules.lov.mapper.LovMapper;
import com.hccake.ballcat.admin.modules.lov.model.Vo.LovVo;
import com.hccake.ballcat.admin.modules.lov.model.entity.Lov;
import com.hccake.ballcat.admin.modules.lov.model.entity.LovBody;
import com.hccake.ballcat.admin.modules.lov.model.entity.LovSearch;
import com.hccake.ballcat.admin.modules.lov.service.LovBodyService;
import com.hccake.ballcat.admin.modules.lov.service.LovSearchService;
import com.hccake.ballcat.admin.modules.lov.service.LovService;
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
public class LovServiceImpl extends ServiceImpl<LovMapper, Lov> implements LovService {

	private final LovBodyService bodyService;

	private final LovSearchService searchService;

	@Override
	public IPage<Lov> selectPage(IPage<Lov> page, Lov entity) {
		return baseMapper.selectPage(page,
				Wrappers.<Lov>lambdaQuery()
						.like(StrUtil.isNotEmpty(entity.getKeyword()), Lov::getKeyword, entity.getKeyword())
						.eq(entity.getMethod() != null, Lov::getMethod, entity.getMethod())
						.eq(entity.getPosition() != null, Lov::getPosition, entity.getPosition())
						.like(StrUtil.isNotEmpty(entity.getUrl()), Lov::getUrl, entity.getUrl()));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean update(Lov lov, List<LovBody> bodyList, List<LovSearch> searchList) {
		if (updateById(lov)) {
			List<Long> removeIds = new ArrayList<>();
			// 获取现有lov body
			List<LovBody> lovBodyList = bodyService
					.list(Wrappers.<LovBody>lambdaQuery().eq(LovBody::getKeyword, lov.getKeyword()));

			// 获取现有的id
			Set<Long> ids = bodyList.stream().map(LovBody::getId).collect(Collectors.toSet());
			// 筛选需要删除的id
			for (LovBody body : lovBodyList) {
				if (!ids.contains(body.getId())) {
					removeIds.add(body.getId());
				}
			}
			bodyService.removeByIds(removeIds);
			bodyService.saveOrUpdateBatch(
					bodyList.stream().map(body -> body.setKeyword(lov.getKeyword())).collect(Collectors.toList()));

			// 清空已有需要删除的id
			removeIds.clear();
			// 获取现有lov body
			List<LovSearch> lovSearchList = searchService
					.list(Wrappers.<LovSearch>lambdaQuery().eq(LovSearch::getKeyword, lov.getKeyword()));

			// 获取现有的id
			ids = searchList.stream().map(LovSearch::getId).collect(Collectors.toSet());
			// 筛选需要删除的id
			for (LovSearch search : lovSearchList) {
				if (!ids.contains(search.getId())) {
					removeIds.add(search.getId());
				}
			}
			searchService.removeByIds(removeIds);
			searchService.saveOrUpdateBatch(
					searchList.stream().map(body -> body.setKeyword(lov.getKeyword())).collect(Collectors.toList()));
			return true;
		}
		return false;
	}

	@Override
	public boolean remove(Integer id) {
		return removeById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save(Lov lov, List<LovBody> bodyList, List<LovSearch> searchList) {
		return save(lov)
				&& bodyService.saveBatch(
						bodyList.stream().map(body -> body.setKeyword(lov.getKeyword())).collect(Collectors.toList()))
				&& searchService.saveBatch(searchList.stream().map(search -> search.setKeyword(lov.getKeyword()))
						.collect(Collectors.toList()));
	}

	@Override
	public LovVo getDataByKeyword(String keyword) {
		Lov lov = baseMapper.selectOne(Wrappers.<Lov>lambdaQuery().eq(Lov::getKeyword, keyword));
		if (lov != null) {
			LovVo vo = new LovVo().setKey(lov.getKey()).setFixedParams(lov.getFixedParams()).setMethod(lov.getMethod())
					.setKeyword(lov.getKeyword()).setMultiple(lov.getMultiple()).setPosition(lov.getPosition())
					.setRet(lov.getRet()).setSearch(lov.getSearch()).setUrl(lov.getUrl()).setRetField(lov.getRetField())
					.setRetFieldDataType(lov.getRetFieldDataType());
			vo.setBodyList(bodyService.list(Wrappers.<LovBody>lambdaQuery().eq(LovBody::getKeyword, lov.getKeyword())));
			vo.setSearchList(
					searchService.list(Wrappers.<LovSearch>lambdaQuery().eq(LovSearch::getKeyword, lov.getKeyword())));
			return vo;
		}
		return null;
	}

}
