package com.hccake.ballcat.system.service.impl;

import com.hccake.ballcat.system.mapper.SysLovSearchMapper;
import com.hccake.ballcat.system.model.entity.SysLovSearch;
import com.hccake.ballcat.system.service.SysLovSearchService;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lingting 2020-08-10 17:21
 */
@Service
public class SysLovSearchServiceImpl extends ExtendServiceImpl<SysLovSearchMapper, SysLovSearch>
		implements SysLovSearchService {

	/**
	 * 根据 lov keyword 查询对应 search
	 * @param keyword lov标识
	 * @return List<LovSearch>
	 */
	@Override
	public List<SysLovSearch> listByKeyword(String keyword) {
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
