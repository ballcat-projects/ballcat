package com.hccake.ballcat.admin.modules.lov.service;

import com.hccake.ballcat.admin.modules.lov.model.entity.Lov;
import com.hccake.ballcat.admin.modules.lov.model.entity.LovBody;
import com.hccake.ballcat.admin.modules.lov.model.entity.LovSearch;
import com.hccake.ballcat.admin.modules.lov.model.qo.LovQO;
import com.hccake.ballcat.admin.modules.lov.model.vo.LovInfoVO;
import com.hccake.ballcat.admin.modules.lov.model.vo.LovVO;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.List;

/**
 * @author lingting 2020-08-10 17:20
 */
public interface LovService extends ExtendService<Lov> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<LoginLogVO> 分页数据
	 */
	PageResult<LovVO> queryPage(PageParam pageParam, LovQO qo);

	/**
	 * 更新实体类
	 * @param entity 实体类
	 * @param bodyList body
	 * @param searchList search
	 * @return boolean
	 * @author lingting 2020-07-21 10:47:24
	 */
	boolean update(Lov entity, List<LovBody> bodyList, List<LovSearch> searchList);

	/**
	 * 移除实体类
	 * @param id id
	 * @return boolean
	 * @author lingting 2020-07-21 10:49:35
	 */
	boolean remove(Integer id);

	/**
	 * 更新实体类
	 * @param lov 实体类
	 * @param bodyList body内容
	 * @param searchList search 内容
	 * @return boolean
	 * @author lingting 2020-07-21 10:47:24
	 */
	boolean save(Lov lov, List<LovBody> bodyList, List<LovSearch> searchList);

	/**
	 * 根据keyword获取lov数据
	 * @param keyword keyword
	 * @return com.hccake.ballcat.admin.modules.lov.model.Vo.LovVo
	 * @author lingting 2020-08-12 21:38
	 */
	LovInfoVO getDataByKeyword(String keyword);

}
