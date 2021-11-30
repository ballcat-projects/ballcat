package com.hccake.ballcat.system.service.impl;

import com.hccake.ballcat.system.mapper.SysLovBodyMapper;
import com.hccake.ballcat.system.model.entity.SysLovBody;
import com.hccake.ballcat.system.service.SysLovBodyService;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lingting 2020-08-10 17:21
 */
@Service
@Deprecated
public class SysLovBodyServiceImpl extends ExtendServiceImpl<SysLovBodyMapper, SysLovBody>
		implements SysLovBodyService {

	/**
	 * 根据 lov keyword 查询对应 body
	 * @param keyword lov标识
	 * @return List<LovBody>
	 */
	@Override
	public List<SysLovBody> listByKeyword(String keyword) {
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
