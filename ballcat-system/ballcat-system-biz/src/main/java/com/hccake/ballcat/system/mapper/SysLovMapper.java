package com.hccake.ballcat.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.system.converter.SysLovConverter;
import com.hccake.ballcat.system.model.entity.SysLov;
import com.hccake.ballcat.system.model.qo.SysLovQO;
import com.hccake.ballcat.system.model.vo.SysLovPageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

/**
 * @author lingting 2020-08-10 17:20
 */
public interface SysLovMapper extends ExtendMapper<SysLov> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return 分页结果数据 PageResult
	 */
	default PageResult<SysLovPageVO> queryPage(PageParam pageParam, SysLovQO qo) {
		IPage<SysLov> page = this.prodPage(pageParam);
		LambdaQueryWrapperX<SysLov> wrapperX = WrappersX.lambdaQueryX(SysLov.class)
				.likeIfPresent(SysLov::getKeyword, qo.getKeyword()).eqIfPresent(SysLov::getMethod, qo.getMethod())
				.eqIfPresent(SysLov::getPosition, qo.getPosition()).likeIfPresent(SysLov::getUrl, qo.getUrl())
				.likeIfPresent(SysLov::getTitle, qo.getTitle());
		this.selectPage(page, wrapperX);
		IPage<SysLovPageVO> voPage = page.convert(SysLovConverter.INSTANCE::poToPageVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	/**
	 * 根据 keyword 查询对应的 lov
	 * @param keyword lov 标识
	 * @return Lov 实体
	 */
	default SysLov selectByKeyword(String keyword) {
		return this.selectOne(Wrappers.<SysLov>lambdaQuery().eq(SysLov::getKeyword, keyword));
	}

}
