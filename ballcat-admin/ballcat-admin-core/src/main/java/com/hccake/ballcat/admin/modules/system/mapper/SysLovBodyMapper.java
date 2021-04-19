package com.hccake.ballcat.admin.modules.system.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.admin.modules.system.model.entity.SysLovBody;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;

import java.util.List;

/**
 * @author lingting 2020-08-10 17:23
 */
public interface SysLovBodyMapper extends ExtendMapper<SysLovBody> {

	/**
	 * 根据 lov keyword 查询对应 body
	 * @param keyword lov标识
	 * @return List<LovBody>
	 */
	default List<SysLovBody> listByKeyword(String keyword) {
		return this.selectList(Wrappers.<SysLovBody>lambdaQuery().eq(SysLovBody::getKeyword, keyword));
	}

	/**
	 * 根据 lov keyword 删除对应 body
	 * @param keyword lov标识
	 * @return 是否删除成功
	 */
	default boolean deleteByKeyword(String keyword) {
		int i = this.delete(Wrappers.<SysLovBody>lambdaQuery().eq(SysLovBody::getKeyword, keyword));
		return SqlHelper.retBool(i);
	}

}
