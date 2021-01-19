package com.hccake.ballcat.admin.modules.lov.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.admin.modules.lov.model.entity.LovBody;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;

import java.util.List;

/**
 * @author lingting 2020-08-10 17:23
 */
public interface LovBodyMapper extends ExtendMapper<LovBody> {

	/**
	 * 根据 lov keyword 查询对应 body
	 * @param keyword lov标识
	 * @return List<LovBody>
	 */
	default List<LovBody> listByKeyword(String keyword) {
		return this.selectList(Wrappers.<LovBody>lambdaQuery().eq(LovBody::getKeyword, keyword));
	}

	/**
	 * 根据 lov keyword 删除对应 body
	 * @param keyword lov标识
	 * @return 是否删除成功
	 */
	default boolean deleteByKeyword(String keyword) {
		int i = this.delete(Wrappers.<LovBody>lambdaQuery().eq(LovBody::getKeyword, keyword));
		return SqlHelper.retBool(i);
	}

}
