package com.hccake.ballcat.admin.modules.lov.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hccake.ballcat.admin.modules.lov.model.entity.Lov;
import com.hccake.ballcat.admin.modules.lov.model.qo.LovQO;
import com.hccake.ballcat.admin.modules.lov.model.vo.LovVO;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.domain.PageResult;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

/**
 * @author lingting 2020-08-10 17:20
 */
public interface LovMapper extends ExtendMapper<Lov> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return 分页结果数据 PageResult
	 */
	default PageResult<LovVO> queryPage(PageParam pageParam, LovQO qo) {
		IPage<LovVO> page = this.prodPage(pageParam);
		LambdaQueryWrapperX<Lov> wrapperX = WrappersX.lambdaQueryX(Lov.class)
				.likeIfPresent(Lov::getKeyword, qo.getKeyword()).eqIfPresent(Lov::getMethod, qo.getMethod())
				.eqIfPresent(Lov::getPosition, qo.getPosition()).likeIfPresent(Lov::getUrl, qo.getUrl())
				.likeIfPresent(Lov::getTitle, qo.getTitle());
		this.selectByPage(page, wrapperX);
		return new PageResult<>(page.getRecords(), page.getTotal());
	}

}
