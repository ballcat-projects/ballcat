package com.hccake.ballcat.admin.modules.system.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.admin.modules.system.model.entity.LovSearch;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;

import java.util.List;

/**
 * @author lingting 2020-08-10 17:20
 */
public interface LovSearchMapper extends ExtendMapper<LovSearch> {

	/**
	 * 根据 lov keyword 查询对应 search
	 * @param keyword lov标识
	 * @return List<LovSearch>
	 */
	default List<LovSearch> listByKeyword(String keyword) {
		return this.selectList(Wrappers.<LovSearch>lambdaQuery().eq(LovSearch::getKeyword, keyword));
	}

	/**
	 * 根据 lov keyword 删除对应 search
	 * @param keyword lov标识
	 * @return 是否删除成功
	 */
	default boolean deleteByKeyword(String keyword) {
		int i = this.delete(Wrappers.<LovSearch>lambdaQuery().eq(LovSearch::getKeyword, keyword));
		return SqlHelper.retBool(i);
	}

}
