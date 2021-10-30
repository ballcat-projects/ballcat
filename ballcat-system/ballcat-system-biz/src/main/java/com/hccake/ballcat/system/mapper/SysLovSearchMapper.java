package com.hccake.ballcat.system.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.system.model.entity.SysLovSearch;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;

import java.util.List;

/**
 * @author lingting 2020-08-10 17:20
 */
@Deprecated
public interface SysLovSearchMapper extends ExtendMapper<SysLovSearch> {

	/**
	 * 根据 lov keyword 查询对应 search
	 * @param keyword lov标识
	 * @return List<LovSearch>
	 */
	default List<SysLovSearch> listByKeyword(String keyword) {
		return this.selectList(Wrappers.<SysLovSearch>lambdaQuery().eq(SysLovSearch::getKeyword, keyword));
	}

	/**
	 * 根据 lov keyword 删除对应 search
	 * @param keyword lov标识
	 * @return 是否删除成功
	 */
	default boolean deleteByKeyword(String keyword) {
		int i = this.delete(Wrappers.<SysLovSearch>lambdaQuery().eq(SysLovSearch::getKeyword, keyword));
		return SqlHelper.retBool(i);
	}

}
