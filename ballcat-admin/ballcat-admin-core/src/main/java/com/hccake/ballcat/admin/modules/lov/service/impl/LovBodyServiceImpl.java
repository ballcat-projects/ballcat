package com.hccake.ballcat.admin.modules.lov.service.impl;

import com.hccake.ballcat.admin.modules.lov.mapper.LovBodyMapper;
import com.hccake.ballcat.admin.modules.lov.model.entity.LovBody;
import com.hccake.ballcat.admin.modules.lov.service.LovBodyService;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lingting 2020-08-10 17:21
 */
@Service
public class LovBodyServiceImpl extends ExtendServiceImpl<LovBodyMapper, LovBody> implements LovBodyService {

	/**
	 * 根据 lov keyword 查询对应 body
	 * @param keyword lov标识
	 * @return List<LovBody>
	 */
	@Override
	public List<LovBody> listByKeyword(String keyword) {
		return baseMapper.listByKeyword(keyword);
	}

	/**
	 * 根据 lov keyword 删除对应 body
	 * @param keyword lov标识
	 * @return 是否删除成功
	 */
	@Override
	public boolean removeByKeyword(String keyword) {
		return baseMapper.deleteByKeyword(keyword);
	}

}
