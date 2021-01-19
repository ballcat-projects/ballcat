package com.hccake.ballcat.admin.modules.lov.service.impl;

import com.hccake.ballcat.admin.modules.lov.mapper.LovSearchMapper;
import com.hccake.ballcat.admin.modules.lov.model.entity.LovSearch;
import com.hccake.ballcat.admin.modules.lov.service.LovSearchService;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lingting 2020-08-10 17:21
 */
@Service
public class LovSearchServiceImpl extends ExtendServiceImpl<LovSearchMapper, LovSearch> implements LovSearchService {

	/**
	 * 根据 lov keyword 查询对应 search
	 * @param keyword lov标识
	 * @return List<LovSearch>
	 */
	@Override
	public List<LovSearch> listByKeyword(String keyword) {
		return baseMapper.listByKeyword(keyword);
	}

	/**
	 * 根据 lov keyword 删除对应 search
	 * @param keyword lov标识
	 * @return 是否删除成功
	 */
	@Override
	public boolean removeByKeyword(String keyword) {
		return baseMapper.deleteByKeyword(keyword);
	}

}
